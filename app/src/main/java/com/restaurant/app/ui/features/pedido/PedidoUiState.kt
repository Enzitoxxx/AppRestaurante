package com.restaurant.app.ui.features.pedido

import com.restaurant.app.domain.model.Pedido

/** Estado de la pantalla de Pedidos. */
data class PedidoUiState(
    val pedidos: List<Pedido> = emptyList(),
    val nombresMesa: Map<String, String> = emptyMap(),       // mesaId -> "Mesa 03"
    val nombresEmpleados: Map<String, String> = emptyMap(),  // empleadoId -> "Camila Gómez"
    val filtro: FiltroPedido = FiltroPedido.ABIERTOS,
    val busqueda: String = ""
) {
    fun nombreMesa(pedido: Pedido): String = nombresMesa[pedido.mesaId].orEmpty()
    fun nombreCamarero(pedido: Pedido): String = nombresEmpleados[pedido.empleadoId].orEmpty()

    /** Aplica el filtro de estado y la búsqueda por mesa o camarero; los más nuevos primero. */
    val pedidosFiltrados: List<Pedido>
        get() {
            val texto = busqueda.trim()
            return pedidos
                .filter { filtro.acepta(it) }
                .filter {
                    texto.isEmpty() ||
                            nombreMesa(it).contains(texto, ignoreCase = true) ||
                            nombreCamarero(it).contains(texto, ignoreCase = true)
                }
                .sortedByDescending { it.fecha }
        }

    /** Pedidos en curso (en preparación, listos o esperando la cuenta). */
    val cantidadAbiertos: Int
        get() = pedidos.count { it.estado.estaAbierto }
}