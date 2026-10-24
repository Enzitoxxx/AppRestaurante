package com.restaurant.app.ui.features.personal.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.model.Empleado
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PersonalRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/*
    1. Recibe los cambios de cada campo y genera el usuario a partir del nombre.
    2. En modo Edición, carga el empleado desde el repositorio.
    3. Valida: nombre y teléfono obligatorios, correo válido y no repetido .
    4. Guarda en el repositorio (alta o edición) y avisa con EmpleadoFormEvent.
*/
class EmpleadoFormViewModel(
    private val repositorio: PersonalRepository = Repositorios.personal,
    private val mesaRepository: MesaRepository = Repositorios.mesas
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmpleadoFormUiState())
    val uiState: StateFlow<EmpleadoFormUiState> = _uiState.asStateFlow()

    private val _events = Channel<EmpleadoFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // null = empleado nuevo. Con valor = el empleado que se está editando.
    private var empleadoOriginal: Empleado? = null

    // modo edicion: busca el empleado en el repositorio y precarga el formulario
    fun cargarEmpleado(id: String) {
        if (empleadoOriginal?.id == id) return   // ya se cargó: no pisar lo que el usuario edita
        viewModelScope.launch {
            val empleado = repositorio.obtenerEmpleado(id) ?: return@launch
            empleadoOriginal = empleado
            _uiState.update {
                it.copy(
                    nombre = empleado.nombre,
                    telefono = empleado.telefono,
                    correo = empleado.correo,
                    rol = empleado.rol,
                    turno = empleado.turno,
                    asignacion = empleado.asignacion,
                    usuarioGenerado = generarUsuario(empleado.nombre),
                    claveTemporalGenerada = CLAVE_TEMPORAL,
                    estaActivo = empleado.estaActivo,
                    esEdicion = true
                )
            }
        }
    }

    fun onNombreChange(value: String) {
        _uiState.update {
            it.copy(
                nombre = value,
                usuarioGenerado = generarUsuario(value),
                claveTemporalGenerada = if (value.isNotBlank()) CLAVE_TEMPORAL else "",
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

    fun onActivoChange(value: Boolean) {
        _uiState.update { it.copy(estaActivo = value) }
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

        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }

            val errorNombre = if (nombre.isBlank()) R.string.empleado_form_error_nombre else null
            val errorTelefono = if (telefono.isBlank()) R.string.empleado_form_error_telefono else null
            val errorCorreo = validarCorreo(correo)

            if (errorNombre != null || errorTelefono != null || errorCorreo != null) {
                _uiState.update {
                    it.copy(
                        estaCargando = false,
                        errorNombre = errorNombre,
                        errorTelefono = errorTelefono,
                        errorCorreo = errorCorreo
                    )
                }
                return@launch
            }

            val original = empleadoOriginal
            val empleado = original?.copy(
                // Edición: cambia lo del formulario y conserva el id y si está activo
                nombre = nombre,
                telefono = telefono,
                correo = correo,
                rol = state.rol,
                turno = state.turno,
                asignacion = state.asignacion,
                estaActivo = state.estaActivo
            ) ?: Empleado(
                // Alta: empleado nuevo, activo y con id nuevo
                id = UUID.randomUUID().toString(),
                nombre = nombre,
                rol = state.rol,
                turno = state.turno,
                asignacion = state.asignacion,
                estaActivo = true,
                telefono = telefono,
                correo = correo
            )

            repositorio.guardarEmpleado(empleado)
            actualizarMesasDelEmpleado(empleado)

            _uiState.update { it.copy(estaCargando = false) }
            _events.send(EmpleadoFormEvent.EmpleadoGuardado)
        }
    }

    // Devuelve el id del texto de error, o null si el correo es válido
    private suspend fun validarCorreo(correo: String): Int? {
        if (correo.isBlank()) return R.string.empleado_form_error_correo
        if (!EMAIL_REGEX.matches(correo)) return R.string.login_error_email_invalid

        // No repetido: ningún OTRO empleado puede tener ese correo
        val empleados = repositorio.observarPersonal().first()
        val repetido = empleados.any {
            it.correo.equals(correo, ignoreCase = true) && it.id != empleadoOriginal?.id
        }
        return if (repetido) R.string.empleado_form_error_correo_repetido else null
    }

    // Si cambió de zona, de rol o se desactivó, se le quitan las mesas que ya no le corresponden
    private suspend fun actualizarMesasDelEmpleado(empleado: Empleado) {
        val atiendeMesas = empleado.estaActivo && empleado.atiendeMesas
        when {
            !atiendeMesas -> mesaRepository.liberarMesasFueraDeZona(empleado.id, null)   // se le quitan todas
            empleado.zonaMesas != null ->
                mesaRepository.liberarMesasFueraDeZona(empleado.id, empleado.zonaMesas)  // se le quitan las de otra zona
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
        const val CLAVE_TEMPORAL = "Mesa2026!"
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}