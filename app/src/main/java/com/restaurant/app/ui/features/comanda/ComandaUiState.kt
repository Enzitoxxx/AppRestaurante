package com.restaurant.app.ui.features.comanda

import com.restaurant.app.domain.model.ComandaModel
import com.restaurant.app.domain.model.EstadoComanda

/** Estado de la pantalla de Gestión de Comandas. */
data class ComandaUiState(
    val filtroEstado: String = "Todos",
    val filtrosDisponibles: List<String> = listOf("Todos", "En preparación", "Cobrados"),
    val comandas: List<ComandaModel> = emptyList()
) {
    /** Filtra la lista según la pestaña seleccionada */
    val comandasFiltradas: List<ComandaModel>
        get() = when (filtroEstado) {
            "En preparación" -> comandas.filter { it.estado == EstadoComanda.EN_PREPARACION }
            "Cobrados" -> comandas.filter { it.estado == EstadoComanda.COBRADO }
            else -> comandas
        }

    /** Cantidad de comandas que están activas (no cobradas) */
    val cantidadActivas: Int
        get() = comandas.count { it.estado != EstadoComanda.COBRADO }
}