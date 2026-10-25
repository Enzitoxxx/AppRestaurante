package com.restaurant.app.ui.features.comanda.detalle

sealed interface DetalleComandaEvent {
    data object ComandaCobrada : DetalleComandaEvent
    data object ComandaAnulada : DetalleComandaEvent
}