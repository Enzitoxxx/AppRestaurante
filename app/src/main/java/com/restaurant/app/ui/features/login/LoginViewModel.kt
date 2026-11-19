package com.restaurant.app.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.repository.AuthRepository
import com.restaurant.app.domain.repository.ResultadoLogin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
 * VIEWMODEL DE INICIO DE SESIÓN (LoginViewModel).
 *
 * El ViewModel coordina las acciones de la pantalla de Login conectándose con `AuthRepository`.
 * Recibe el repositorio por constructor con un valor por defecto (= Repositorios.auth), facilitando
 * la inyección de dependencias y las pruebas unitarias.
 *
 * Mantiene la regla del proyecto:
 * 1. UiState inmutable con StateFlow para mantener el estado gráfico.
 * 2. Channel BUFFERED expuesto como Flow para los eventos únicos de navegación (LoginSuccess).
 */
class LoginViewModel(
    private val authRepository: AuthRepository = Repositorios.auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val state = _uiState.value
        if (state.isLoading) return

        val email = state.email.trim()
        val emailError = when {
            email.isBlank() -> R.string.login_error_email_required
            !EMAIL_REGEX.matches(email) -> R.string.login_error_email_invalid
            else -> null
        }
        val passwordError =
            if (state.password.isBlank()) R.string.login_error_password_required else null

        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Llamada real al repositorio de autenticación (AuthRepository)
            val resultado = authRepository.iniciarSesion(email, state.password)

            _uiState.update { it.copy(isLoading = false) }

            when (resultado) {
                is ResultadoLogin.Exito -> {
                    _events.send(LoginEvent.LoginSuccess)
                }
                ResultadoLogin.CredencialesInvalidas -> {
                    _uiState.update {
                        it.copy(emailError = R.string.login_error_credenciales_invalidas)
                    }
                }
                ResultadoLogin.CuentaInactiva -> {
                    _uiState.update {
                        it.copy(emailError = R.string.login_error_cuenta_inactiva)
                    }
                }
            }
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
