package com.restaurant.app.domain.model

data class ComandaItemModel(
    val id: String,
    val cantidad: Int,
    val nombre: String,
    val precioUnitario: Double,
    val observacion: String? = null
) {
    val precioTotal: Double
        get() = cantidad * precioUnitario
}