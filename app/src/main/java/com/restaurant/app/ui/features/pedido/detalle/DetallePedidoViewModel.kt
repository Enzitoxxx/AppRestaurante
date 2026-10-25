package com.restaurant.app.ui.features.pedido.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PedidoRepository
import com.restaurant.app.domain.repository.PersonalRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetallePedidoViewModel(
    private val pedidoRepository: PedidoRepository = Repositorios.pedidos,
    private val mesaRepository: MesaRepository = Repositorios.mesas,
    private val personalRepository: PersonalRepository = Repositorios.personal
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetallePedidoUiState())
    val uiState: StateFlow<DetallePedidoUiState> = _uiState.asStateFlow()

    private val _events = Channel<DetallePedidoEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var pedidoId: String? = null

    // Busca el pedido por id, con el nombre de su mesa y de su camarero (RF14)
    fun cargarPedido(id: String) {
        if (pedidoId == id) return
        pedidoId = id
        viewModelScope.launch {
            val pedido = pedidoRepository.obtenerPedido(id) ?: return@launch
            val mesa = mesaRepository.obtenerMesa(pedido.mesaId)
            val empleado = personalRepository.obtenerEmpleado(pedido.empleadoId)
            _uiState.update {
                it.copy(
                    pedido = pedido,
                    nombreMesa = mesa?.nombre.orEmpty(),
                    nombreCamarero = empleado?.nombre.orEmpty()
                )
            }
        }
    }

    fun onCobrarClick() {
        val state = _uiState.value
        val pedido = state.pedido ?: return
        if (state.estaProcesando || !pedido.estado.estaAbierto) return

        viewModelScope.launch {
            _uiState.update { it.copy(estaProcesando = true) }
            val cobrado = pedidoRepository.cobrarPedido(pedido.id)
            _uiState.update { it.copy(estaProcesando = false) }
            if (cobrado) _events.send(DetallePedidoEvent.PedidoCobrado)
        }
    }

    fun onAbrirAnularSheet() {
        // Cada vez que se abre, el motivo arranca sin elegir
        _uiState.update { it.copy(mostrarAnularSheet = true, motivoSeleccionado = null) }
    }

    fun onCerrarAnularSheet() {
        _uiState.update { it.copy(mostrarAnularSheet = false) }
    }

    fun onMotivoSeleccionado(motivo: String) {
        _uiState.update { it.copy(motivoSeleccionado = motivo) }
    }

    fun onConfirmarAnulacion() {
        val state = _uiState.value
        val pedido = state.pedido ?: return
        val motivo = state.motivoSeleccionado ?: return          // RF15: sin motivo no se anula
        if (state.estaProcesando || !pedido.estado.estaAbierto) return  // RF15: solo pedidos abiertos

        viewModelScope.launch {
            _uiState.update { it.copy(estaProcesando = true, mostrarAnularSheet = false) }
            val anulado = pedidoRepository.anularPedido(pedido.id, motivo, USUARIO_ACTUAL)
            _uiState.update { it.copy(estaProcesando = false) }
            if (anulado) _events.send(DetallePedidoEvent.PedidoAnulado)
        }
    }

    private companion object {
        // TODO: tomar el usuario de la sesión cuando el login esté conectado (RF01 y RF04)
        const val USUARIO_ACTUAL = "Administrador"
    }
}