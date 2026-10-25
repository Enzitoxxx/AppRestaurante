package com.restaurant.app.domain.model

data class ItemPedido(
    val id: String,
    val productoId: String,          // para saber cuál es el más vendido (RF05)
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Double,      // precio al momento del pedido, aunque el producto cambie después
    val observacion: String? = null
) {
    val precioTotal: Double
        get() = cantidad * precioUnitario
}