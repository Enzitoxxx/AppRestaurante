package com.restaurant.app.ui.features.mas

sealed interface MasEvent {
    data object SesionCerrada : MasEvent
}
