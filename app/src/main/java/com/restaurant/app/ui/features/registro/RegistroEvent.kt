package com.restaurant.app.ui.features.registro

sealed interface RegistroEvent {
    data object RegistroSuccess : RegistroEvent
}