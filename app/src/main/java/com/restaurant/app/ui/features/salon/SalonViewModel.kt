package com.restaurant.app.ui.features.salon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.model.Mesa
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PedidoRepository
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
    private val mesaRepository: MesaRepository = Repositorios.mesas,
    private val personalRepository: PersonalRepository = Repositorios.personal,
    private val pedidoRepository: PedidoRepository = Repositorios.pedidos
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalonUiState())
    val uiState: StateFlow<SalonUiState> = _uiState.asStateFlow()

    private val _events = Channel<SalonEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        // Escuchamos mesas, personal y pedidos juntos: si cambia cualquiera, se actualiza el Salón
        viewModelScope.launch {
            combine(
                mesaRepository.observarMesas(),
                personalRepository.observarPersonal(),
                pedidoRepository.observarPedidos()
            ) { mesas, personal, pedidos ->
                Triple(mesas, personal.associate { it.id to it.nombreCorto }, pedidos)
            }.collect { (mesas, nombres, pedidos) ->
                _uiState.update {
                    it.copy(
                        mesas = mesas,
                        nombresEmpleados = nombres,
                        pedidos = pedidos
                    )
                }
            }
        }
    }

    fun onZonaSeleccionada(zona: String) {
        _uiState.update { it.copy(zonaSeleccionada = zona) }
    }

    fun onMesaClick(mesa: Mesa) {
        val pedidoAbierto = _uiState.value.pedidos.find { it.mesaId == mesa.id && it.estado.estaAbierto }
        if (pedidoAbierto != null) {
            _events.trySend(SalonEvent.AbrirDetallePedido(pedidoAbierto.id))
        } else {
            _events.trySend(SalonEvent.MostrarSnackbar("La mesa no tiene un pedido abierto"))
        }
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
        _uiState.update { it.copy(mesaADarDeBaja = null) }

        val tienePedidoAbierto = _uiState.value.pedidos.any { it.mesaId == mesaAEliminar.id && it.estado.estaAbierto }
        if (tienePedidoAbierto) {
            _events.trySend(SalonEvent.MostrarSnackbar("No se puede eliminar la ${mesaAEliminar.nombre}: tiene un pedido abierto"))
            return
        }

        viewModelScope.launch {
            mesaRepository.eliminarMesa(mesaAEliminar.id)
        }
    }
}
