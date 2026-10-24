package com.restaurant.app.ui.features.personal.asignacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.repository.MesaRepositoryDatos
import com.restaurant.app.domain.repository.MesaRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * VIEWMODEL DE LA PANTALLA ASIGNACIÓN DE MESAS.
 *
 * Administra la selección de mesas a cargo para un empleado específico (ej: Camarera Camila Gómez).
 * Consume las mesas del repositorio (MesaRepository) en tiempo real.
 */
class AsignacionMesasViewModel(
    private val mesaRepository: MesaRepository = MesaRepositoryDatos()
) : ViewModel() {

    // Estado interno mutable (privado) y público de solo lectura (StateFlow)
    private val _uiState = MutableStateFlow(AsignacionMesasUiState())
    val uiState: StateFlow<AsignacionMesasUiState> = _uiState.asStateFlow()

    // Canal para eventos de acción única (One-Shot Events)
    private val _events = Channel<AsignacionMesasEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        // Carga las mesas en tiempo real desde la capa de datos
        viewModelScope.launch {
            mesaRepository.observarMesas().collect { listaMesas ->
                _uiState.update { it.copy(mesasDisponibles = listaMesas) }
            }
        }
    }

    /*
     * Carga el nombre y rol del empleado al abrir la pantalla.
     */
    fun cargarEmpleado(nombre: String, rol: String) {
        _uiState.update {
            it.copy(
                nombreEmpleado = nombre,
                rolEmpleado = rol
            )
        }
    }

    /*
     * Alterna la selección de una mesa (Checkbox).
     * Si la mesa ya estaba seleccionada, la remueve del Set.
     * Si no estaba seleccionada, la agrega al Set.
     */
    fun onToggleMesaSeleccionada(mesaId: String) {
        _uiState.update { state ->
            val setActual = state.idsMesasSeleccionadas.toMutableSet()
            if (setActual.contains(mesaId)) {
                setActual.remove(mesaId)
            } else {
                setActual.add(mesaId)
            }
            state.copy(idsMesasSeleccionadas = setActual)
        }
    }

    /*
     * Solicitud de retorno (botón '←').
     */
    fun onVolverClick() {
        _events.trySend(AsignacionMesasEvent.Volver)
    }

    /*
     * Confirma y guarda la asignación de mesas seleccionadas en segundo plano.
     */
    fun onGuardarAsignacionClick() {
        val state = _uiState.value
        if (state.estaCargando) return

        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            delay(1000) // Simulación de guardado en la BD / servidor
            _uiState.update { it.copy(estaCargando = false) }
            _events.send(AsignacionMesasEvent.AsignacionGuardada)
        }
    }
}
