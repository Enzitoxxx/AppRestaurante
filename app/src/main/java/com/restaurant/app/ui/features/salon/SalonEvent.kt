package com.restaurant.app.ui.features.salon

import com.restaurant.app.data.model.Mesa

sealed interface SalonEvent {
    data class AbrirMesa(val mesa: Mesa) : SalonEvent
    data object NuevaMesa : SalonEvent
}