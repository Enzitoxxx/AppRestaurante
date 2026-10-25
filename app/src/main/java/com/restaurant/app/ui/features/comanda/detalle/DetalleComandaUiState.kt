package com.restaurant.app.ui.features.comanda.detalle

import com.restaurant.app.domain.model.ComandaItemModel

data class DetalleComandaUiState(
    val comandaId: String = "124",
    val numeroMesa: String = "03",
    val camareroNombre: String = "Camila Gómez",
    val cantidadComensales: Int = 4,
    val items: List<ComandaItemModel> = emptyList(),
    val subtotal: Double = 28600.0,
    val total: Double = 28600.0,
    val mostrarAnularSheet: Boolean = false,
    val motivoAnulacionSeleccionado: String = "Error de carga por el personal",
    val motivosAnulacion: List<String> = listOf(
        "Error de carga por el personal",
        "Cancelado por el cliente",
        "Falta de stock en cocina"
    ),
    val isLoading: Boolean = false
)