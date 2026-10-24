package com.restaurant.app.ui.features.personal

import com.restaurant.app.domain.model.Empleado

sealed interface PersonalEvent {
    data class EditarEmpleado(val empleado: Empleado) : PersonalEvent
    data class AsignarMesasEmpleado(val empleado: Empleado) : PersonalEvent
    data object AgregarEmpleado : PersonalEvent
}
