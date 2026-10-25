package com.restaurant.app.domain.model

enum class EstadoPedido {
    EN_PREPARACION,
    LISTO_PARA_SERVIR,
    ESPERANDO_CUENTA,
    COBRADO,
    ANULADO;

    // Abiertos = en curso. Cobrado y anulado forman el historial (RF13).
    val estaAbierto: Boolean
        get() = this == EN_PREPARACION || this == LISTO_PARA_SERVIR || this == ESPERANDO_CUENTA
}

data class Pedido(
    val id: String,
    val mesaId: String,
    val empleadoId: String,          // camarero con el que se tomó el pedido
    val comensales: Int,
    val fecha: Long,                 // milisegundos
    val items: List<ItemPedido>,
    val estado: EstadoPedido,
    // Datos de la anulación (RF15): solo tienen valor si el pedido está anulado
    val motivoAnulacion: String? = null,
    val fechaAnulacion: Long? = null,
    val anuladoPor: String? = null
) {
    // El total se calcula con los productos, así nunca puede quedar inconsistente
    val total: Double
        get() = items.sumOf { it.precioTotal }

    val cantidadItems: Int
        get() = items.sumOf { it.cantidad }
}