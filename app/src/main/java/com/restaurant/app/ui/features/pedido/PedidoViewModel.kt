package com.restaurant.app.ui.features.pedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PedidoRepository
import com.restaurant.app.domain.repository.PersonalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PedidoViewModel(
    private val pedidoRepository: PedidoRepository = Repositorios.pedidos,
    private val mesaRepository: MesaRepository = Repositorios.mesas,
    private val personalRepository: PersonalRepository = Repositorios.personal
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoUiState())
    val uiState: StateFlow<PedidoUiState> = _uiState.asStateFlow()

    init {
        // Si cambia un pedido, una mesa o un empleado, la lista se actualiza sola
        viewModelScope.launch {
            combine(
                pedidoRepository.observarPedidos(),
                mesaRepository.observarMesas(),
                personalRepository.observarPersonal()
            ) { pedidos, mesas, personal ->
                Triple(
                    pedidos,
                    mesas.associate { it.id to it.nombre },
                    personal.associate { it.id to it.nombre }
                )
            }.collect { (pedidos, nombresMesa, nombresEmpleados) ->
                _uiState.update {
                    it.copy(
                        pedidos = pedidos,
                        nombresMesa = nombresMesa,
                        nombresEmpleados = nombresEmpleados
                    )
                }
            }
        }
    }

    fun onFiltroSelected(filtro: FiltroPedido) {
        _uiState.update { it.copy(filtro = filtro) }
    }

    fun onBusquedaChange(texto: String) {
        _uiState.update { it.copy(busqueda = texto) }
    }
}