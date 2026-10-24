package com.restaurant.app.ui.features.salon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
//import com.restaurant.app.data.repository.MesaRepositoryDatos
import com.restaurant.app.domain.model.Mesa
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PersonalRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SalonViewModel(
    //private val mesaRepository: MesaRepository = MesaRepositoryDatos()
    private val mesaRepository: MesaRepository = Repositorios.mesas,
    private val personalRepository: PersonalRepository = Repositorios.personal
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalonUiState())
    val uiState: StateFlow<SalonUiState> = _uiState.asStateFlow()

    private val _events = Channel<SalonEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        // Escuchamos mesas y personal juntos: si cambia cualquiera de los dos, se actualiza el Salón
        viewModelScope.launch {
            combine(
                mesaRepository.observarMesas(),
                personalRepository.observarPersonal()
            ) { mesas, personal ->
                mesas to personal.associate { it.id to it.nombreCorto }
            }.collect { (mesas, nombres) ->
                _uiState.update { it.copy(mesas = mesas, nombresEmpleados = nombres) }
            }
        }
    }

    fun onZonaSeleccionada(zona: String) {
        _uiState.update { it.copy(zonaSeleccionada = zona) }
    }

    fun onMesaClick(mesa: Mesa) {
        _events.trySend(SalonEvent.AbrirMesa(mesa))
    }

    fun onNuevaMesaClick() {
        _events.trySend(SalonEvent.NuevaMesa)
    }

    fun onEditarMesaClick(mesa: Mesa) {
        _events.trySend(SalonEvent.EditarMesa(mesa))
    }

    fun onSolicitarBajaMesa(mesa: Mesa) {
        _uiState.update { it.copy(mesaADarDeBaja = mesa) }
    }

    fun onCancelarBajaMesa() {
        _uiState.update { it.copy(mesaADarDeBaja = null) }
    }

    fun onConfirmarBajaMesa() {
        val mesaAEliminar = _uiState.value.mesaADarDeBaja ?: return
        viewModelScope.launch {
            mesaRepository.eliminarMesa(mesaAEliminar.id)
            _uiState.update { it.copy(mesaADarDeBaja = null) }
        }
    }
}
