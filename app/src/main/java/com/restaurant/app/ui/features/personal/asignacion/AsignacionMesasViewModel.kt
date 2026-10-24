package com.restaurant.app.ui.features.personal.asignacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PersonalRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
 * Administra la selección de mesas a cargo de un camarero.
 * 1. Carga el empleado desde el repositorio y marca las mesas que ya tiene asignadas.
 * 2. Permite marcar y desmarcar mesas .
 * 3. Guarda la asignación en el repositorio .
 */
class AsignacionMesasViewModel(
    private val mesaRepository: MesaRepository = Repositorios.mesas,
    private val personalRepository: PersonalRepository = Repositorios.personal
) : ViewModel() {

    private val _uiState = MutableStateFlow(AsignacionMesasUiState())
    val uiState: StateFlow<AsignacionMesasUiState> = _uiState.asStateFlow()

    private val _events = Channel<AsignacionMesasEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var empleadoId: String? = null


    init {
        viewModelScope.launch {
            combine(
                mesaRepository.observarMesas(),
                personalRepository.observarPersonal()
            ) { mesas, personal ->
                mesas to personal.associate { it.id to it.nombreCorto }
            }.collect { (mesas, nombres) ->
                _uiState.update { it.copy(mesasDisponibles = mesas, nombresEmpleados = nombres) }
            }
        }
    }

    // Busca el empleado y preselecciona las mesas que ya tiene a su cargo (solo de su zona)
    fun cargarEmpleado(id: String) {
        if (empleadoId == id) return   // ya se cargó: no pisar lo que el usuario marcó
        empleadoId = id
        viewModelScope.launch {
            val empleado = personalRepository.obtenerEmpleado(id) ?: return@launch
            val zona = empleado.zonaMesas
            val mesas = mesaRepository.observarMesas().first()
            _uiState.update {
                it.copy(
                    idEmpleado = id,
                    zonaMesas = zona,
                    nombreEmpleado = empleado.nombre,
                    rolEmpleado = empleado.rol,
                    idsMesasSeleccionadas = mesas
                        .filter { mesa ->
                            mesa.empleadoAsignadoId == id &&
                                    (zona == null || mesa.zona.equals(zona, ignoreCase = true))
                        }
                        .map { mesa -> mesa.id }
                        .toSet()
                )
            }
        }
    }

    fun onToggleMesaSeleccionada(mesaId: String) {
        _uiState.update { state ->
            val mesa = state.mesasDisponibles.find { it.id == mesaId }
            // Una mesa de otro camarero no se puede marcar (RF12)
            if (mesa != null && state.asignadaAOtro(mesa) != null) return@update state

            val setActual = state.idsMesasSeleccionadas.toMutableSet()
            if (setActual.contains(mesaId)) setActual.remove(mesaId) else setActual.add(mesaId)
            state.copy(idsMesasSeleccionadas = setActual)
        }
    }

    fun onVolverClick() {
        _events.trySend(AsignacionMesasEvent.Volver)
    }

    fun onGuardarAsignacionClick() {
        val state = _uiState.value
        val id = empleadoId ?: return
        if (state.estaCargando) return

        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            mesaRepository.asignarMesas(id, state.idsMesasSeleccionadas)
            _uiState.update { it.copy(estaCargando = false) }
            _events.send(AsignacionMesasEvent.AsignacionGuardada)
        }
    }
}