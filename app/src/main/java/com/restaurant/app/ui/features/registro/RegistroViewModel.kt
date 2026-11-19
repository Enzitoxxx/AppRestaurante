package com.restaurant.app.ui.features.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.repository.AuthRepository
import com.restaurant.app.domain.repository.ResultadoRegistro
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
 * VIEWMODEL DE REGISTRO DE USUARIO (RegistroViewModel).
 *
 * Gestiona la lógica del formulario de registro de un nuevo administrador y su restaurante.
 * Recibe `AuthRepository` por constructor con un valor por defecto (= Repositorios.auth).
 * Valida los datos en la UI y luego intenta llamar a `authRepository.registrar(...)`.
 * Si el correo ya está registrado en Room, muestra el error `registro_error_correo_existente` en rojo.
 */
class RegistroViewModel(
    private val authRepository: AuthRepository = Repositorios.auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    private val _events = Channel<RegistroEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, errorNombre = null) }
    }

    fun onCorreoChange(value: String) {
        _uiState.update { it.copy(correo = value, errorCorreo = null) }
    }

    fun onTelefonoChange(value: String) {
        _uiState.update { it.copy(telefono = value, errorTelefono = null) }
    }

    fun onRestauranteChange(value: String) {
        _uiState.update { it.copy(restaurante = value, errorRestaurante = null) }
    }

    fun onContrasenaChange(value: String) {
        _uiState.update { it.copy(contrasena = value, errorContrasena = null) }
    }

    fun onToggleContrasenaVisibility() {
        _uiState.update { it.copy(contrasenaVisible = !it.contrasenaVisible) }
    }

    fun onRegistroClick() {
        val state = _uiState.value
        if (state.estaCargado) return

        val nombre = state.nombre.trim()
        val correo = state.correo.trim()
        val telefono = state.telefono.trim()
        val restaurante = state.restaurante.trim()
        val contrasena = state.contrasena

        val errorNombre = if (nombre.isBlank()) R.string.registro_error_nombre_requerido else null

        val errorCorreo = when {
            correo.isBlank() -> R.string.login_error_email_required
            !EMAIL_REGEX.matches(correo) -> R.string.login_error_email_invalid
            else -> null
        }

        val errorTelefono = when {
            telefono.isBlank() -> R.string.registro_error_telefono_requerido
            telefono.count { it.isDigit() } < 8 -> R.string.registro_error_telefono_invalido
            else -> null
        }

        val errorRestaurante = if (restaurante.isBlank()) R.string.registro_error_restaurante_requerido else null

        val errorContrasena = when {
            contrasena.isBlank() -> R.string.login_error_password_required
            contrasena.length < 8 -> R.string.registro_error_contrasena_corta
            !contrasena.any { it.isLetter() } || !contrasena.any { it.isDigit() } -> R.string.registro_error_contrasena_formato
            else -> null
        }

        if (errorNombre != null || errorCorreo != null || errorTelefono != null ||
            errorRestaurante != null || errorContrasena != null
        ) {
            _uiState.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorCorreo = errorCorreo,
                    errorTelefono = errorTelefono,
                    errorRestaurante = errorRestaurante,
                    errorContrasena = errorContrasena
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(estaCargado = true) }

            // Llamada real al repositorio para registrar en Room
            val resultado = authRepository.registrar(
                nombre = nombre,
                correo = correo,
                telefono = telefono,
                restauranteNombre = restaurante,
                contrasena = contrasena
            )

            _uiState.update { it.copy(estaCargado = false) }

            when (resultado) {
                is ResultadoRegistro.Exito -> {
                    _uiState.update { it.copy(mostrarConfirmacion = true) }
                }
                ResultadoRegistro.CorreoYaRegistrado -> {
                    _uiState.update {
                        it.copy(errorCorreo = R.string.registro_error_correo_existente)
                    }
                }
            }
        }
    }

    fun onConfirmarRegistro() {
        _uiState.update { it.copy(mostrarConfirmacion = false) }
        viewModelScope.launch {
            _events.send(RegistroEvent.RegistroSuccess)
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
