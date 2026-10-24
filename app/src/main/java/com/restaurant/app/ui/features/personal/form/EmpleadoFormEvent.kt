package com.restaurant.app.ui.features.personal.form

sealed interface EmpleadoFormEvent {
    data object EmpleadoGuardado : EmpleadoFormEvent
    data object Volver : EmpleadoFormEvent
}
