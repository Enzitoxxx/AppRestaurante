package com.restaurant.app.ui.features.personal.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmpleadoFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EmpleadoFormUiState())
    val uiState: StateFlow<EmpleadoFormUiState> = _uiState.asStateFlow()

    private val _events = Channel<EmpleadoFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // Precarga datos para MODO EDICIÓN
    fun cargarEmpleadoParaEdicion(
        nombre: String,
        telefono: String,
        correo: String,
        rol: String,
        turno: String = "Turno Mañana",
        asignacion: String = "Salón Principal"
    ) {
        val usuario = generarUsuario(nombre)
        _uiState.update {
            it.copy(
                nombre = nombre,
                telefono = telefono,
                correo = correo,
                rol = rol,
                turno = turno,
                asignacion = asignacion,
                usuarioGenerado = usuario,
                claveTemporalGenerada = "Mesa2026!",
                esEdicion = true
            )
        }
    }

    fun onNombreChange(value: String) {
        val usuarioNuevo = generarUsuario(value)
        _uiState.update {
            it.copy(
                nombre = value,
                usuarioGenerado = usuarioNuevo,
                claveTemporalGenerada = if (value.isNotBlank()) "Mesa2026!" else "",
                errorNombre = null
            )
        }
    }

    fun onTelefonoChange(value: String) {
        _uiState.update { it.copy(telefono = value, errorTelefono = null) }
    }

    fun onCorreoChange(value: String) {
        _uiState.update { it.copy(correo = value, errorCorreo = null) }
    }

    fun onRolChange(value: String) {
        val asignacionPredeterminada = when {
            value.contains("Camarer", ignoreCase = true) || value.contains("Mozo", ignoreCase = true) -> "Salón Principal"
            value.contains("Bartender", ignoreCase = true) -> "Barra"
            value.contains("Cocinero", ignoreCase = true) -> "Cocina"
            else -> "Salón Principal"
        }
        _uiState.update { it.copy(rol = value, asignacion = asignacionPredeterminada) }
    }

    fun onTurnoChange(value: String) {
        _uiState.update { it.copy(turno = value) }
    }

    fun onAsignacionChange(value: String) {
        _uiState.update { it.copy(asignacion = value) }
    }

    fun onVolverClick() {
        _events.trySend(EmpleadoFormEvent.Volver)
    }

    fun onGuardarClick() {
        val state = _uiState.value
        if (state.estaCargando) return

        val nombre = state.nombre.trim()
        val telefono = state.telefono.trim()
        val correo = state.correo.trim()

        // Validaciones del formulario
        val errorNombre = if (nombre.isBlank()) R.string.empleado_form_error_nombre else null
        val errorTelefono = if (telefono.isBlank()) R.string.empleado_form_error_telefono else null
        val errorCorreo = when {
            correo.isBlank() -> R.string.empleado_form_error_correo
            !EMAIL_REGEX.matches(correo) -> R.string.login_error_email_invalid
            else -> null
        }

        if (errorNombre != null || errorTelefono != null || errorCorreo != null) {
            _uiState.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorTelefono = errorTelefono,
                    errorCorreo = errorCorreo
                )
            }
            return
        }

        // Simulación de guardado en segundo plano
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            delay(1000) // Simulación de llamada a servidor / BD
            _uiState.update { it.copy(estaCargando = false) }
            _events.send(EmpleadoFormEvent.EmpleadoGuardado)
        }
    }

    private fun generarUsuario(nombre: String): String {
        val partes = nombre.trim().lowercase().split("\\s+".toRegex())
        return if (partes.size >= 2) {
            "${partes[0]}.${partes[1].replace("[^a-z0-9]".toRegex(), "")}"
        } else if (partes.isNotEmpty() && partes[0].isNotBlank()) {
            "${partes[0]}.mesa"
        } else {
            ""
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
