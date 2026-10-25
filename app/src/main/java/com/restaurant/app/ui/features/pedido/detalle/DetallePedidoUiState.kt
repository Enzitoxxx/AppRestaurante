package com.restaurant.app.ui.features.pedido.detalle

import com.restaurant.app.domain.model.Pedido

data class DetallePedidoUiState(
    val pedido: Pedido? = null,              // null mientras se carga
    val nombreMesa: String = "",
    val nombreCamarero: String = "",
    val mostrarAnularSheet: Boolean = false,
    val motivoSeleccionado: String? = null,  // sin motivo elegido no se puede anular (RF15)
    val motivosAnulacion: List<String> = listOf(
        "Error de carga por el personal",
        "Cancelado por el cliente",
        "Falta de stock en cocina"
    ),
    val estaProcesando: Boolean = false
)