package com.restaurant.app.ui.features.recuperarcontrasena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
 * VIEWMODEL DE RECUPERACIÓN DE CONTRASEÑA (RecuperarContrasenaViewModel).
 *
 * Maneja el flujo de recuperar contraseña verificando mediante `AuthRepository` si el correo existe.
 * Recibe `AuthRepository` por constructor con un valor por defecto (= Repositorios.auth).
 *
 * NOTA SEGURIDAD:
 * En un sistema real de producción en la nube, por motivos de seguridad NUNCA se debe informar
 * al usuario si un correo existe o no (para evitar que atacantes descubran qué correos están registrados).
 * En este proyecto nosotros mostramos el mensaje de error si el correo no existe para fines de prueba.
 */
class RecuperarContrasenaViewModel(
    private val authRepository: AuthRepository = Repositorios.auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecuperarContrasenaUiState())
    val uiState: StateFlow<RecuperarContrasenaUiState> = _uiState.asStateFlow()

    private val _events = Channel<RecuperarContrasenaEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun onSendClick() {
        val state = _uiState.value
        if (state.isLoading) return

        val email = state.email.trim()
        val emailError = when {
            email.isBlank() -> R.string.forgot_password_error_email_required
            !EMAIL_REGEX.matches(email) -> R.string.forgot_password_error_email_invalid
            else -> null
        }

        if (emailError != null) {
            _uiState.update { it.copy(emailError = emailError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val existe = authRepository.recuperarContrasena(email)

            _uiState.update { it.copy(isLoading = false) }

            if (existe) {
                _uiState.update { it.copy(mostrarMensajeExito = true) }
            } else {
                _uiState.update {
                    it.copy(emailError = R.string.forgot_password_error_email_not_found)
                }
            }
        }
    }

    fun onConfirmarExito() {
        _uiState.update { it.copy(mostrarMensajeExito = false) }
        viewModelScope.launch {
            _events.send(RecuperarContrasenaEvent.ResetPasswordEmailSent)
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
