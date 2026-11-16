package com.restaurant.app.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.model.EstadoMesa
import com.restaurant.app.domain.model.EstadoPedido
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeViewModel(
    private val pedidoRepository: PedidoRepository = Repositorios.pedidos,
    private val mesaRepository: MesaRepository = Repositorios.mesas
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            fechaHoy = obtenerFechaActualFormateada()
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                pedidoRepository.observarPedidos(),
                mesaRepository.observarMesas()
            ) { pedidos, mesas ->
                pedidos to mesas
            }.collect { (pedidos, mesas) ->
                val ahora = System.currentTimeMillis()
                val inicioHoy = Calendar.getInstance().apply {
                    timeInMillis = ahora
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                // Pedidos cobrados hoy (excluye anulados y de fechas anteriores)
                val cobradosHoy = pedidos.filter {
                    it.estado == EstadoPedido.COBRADO && it.fecha >= inicioHoy
                }

                val totalVentasHoy = cobradosHoy.sumOf { it.total }
                val totalTransacciones = cobradosHoy.size
                val pedidosAbiertos = pedidos.count { it.estado.estaAbierto }

                val mesasOcupadas = mesas.count {
                    it.estado == EstadoMesa.OCUPADA || it.estado == EstadoMesa.CUENTA
                }
                val totalMesas = mesas.size

                // Unidades vendidas hoy por cada producto en los pedidos cobrados hoy
                val unidadesPorPlato = mutableMapOf<String, Pair<Int, Double>>()
                cobradosHoy.flatMap { it.items }.forEach { item ->
                    val actual = unidadesPorPlato.getOrDefault(item.nombre, Pair(0, 0.0))
                    unidadesPorPlato[item.nombre] = Pair(
                        actual.first + item.cantidad,
                        actual.second + item.precioTotal
                    )
                }

                val topPlatos = unidadesPorPlato.entries
                    .sortedByDescending { it.value.first }
                    .take(3)
                    .mapIndexed { index, entry ->
                        TopPlatoModel(
                            ranking = index + 1,
                            nombre = entry.key,
                            cantidadVendida = entry.value.first,
                            totalRecaudado = entry.value.second
                        )
                    }

                _uiState.update { state ->
                    state.copy(
                        fechaHoy = obtenerFechaActualFormateada(),
                        totalVentasHoy = totalVentasHoy,
                        totalTransacciones = totalTransacciones,
                        pedidosAbiertos = pedidosAbiertos,
                        mesasOcupadas = mesasOcupadas,
                        totalMesas = totalMesas,
                        topPlatos = topPlatos,
                        porcentajeComparativa = null
                    )
                }
            }
        }
    }

    private fun obtenerFechaActualFormateada(): String {
        val formatter = SimpleDateFormat("d 'de' MMM. yyyy", Locale("es", "AR"))
        return formatter.format(Date())
    }
}
