package com.restaurant.app.ui.features.carta

import com.restaurant.app.domain.model.Producto

sealed interface CartaEvent {
    data object NavegarAAgregarProducto : CartaEvent
    data class NavegarAEditarProducto(val producto: Producto) : CartaEvent
}
