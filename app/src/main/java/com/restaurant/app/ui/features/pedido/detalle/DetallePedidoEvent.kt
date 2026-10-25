package com.restaurant.app.ui.features.pedido.detalle

sealed interface DetallePedidoEvent {
    data object PedidoCobrado : DetallePedidoEvent
    data object PedidoAnulado : DetallePedidoEvent
}