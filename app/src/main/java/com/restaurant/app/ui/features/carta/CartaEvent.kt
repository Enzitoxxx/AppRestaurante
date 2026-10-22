package com.restaurant.app.ui.features.carta

sealed interface CartaEvent {
    data object NavegarAAgregarProducto : CartaEvent
}