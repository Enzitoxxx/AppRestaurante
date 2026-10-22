package com.restaurant.app.ui.features.producto

sealed interface ProductoEvent {
    data object ProductoGuardado : ProductoEvent
    data object  Volver : ProductoEvent
}