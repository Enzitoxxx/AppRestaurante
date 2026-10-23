package com.restaurant.app.ui.features.salon

import com.restaurant.app.domain.model.Mesa

sealed interface SalonEvent {
    data class AbrirMesa(val mesa: Mesa) : SalonEvent
    data class EditarMesa(val mesa: Mesa) : SalonEvent
    data object NuevaMesa : SalonEvent
}
