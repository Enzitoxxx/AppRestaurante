package com.restaurant.app.ui.features.mesa

sealed interface MesaFormEvent {
    data object MesaGuardada : MesaFormEvent
    data object Volver : MesaFormEvent
}