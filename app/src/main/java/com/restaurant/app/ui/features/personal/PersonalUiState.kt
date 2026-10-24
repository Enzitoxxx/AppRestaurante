package com.restaurant.app.ui.features.personal

import com.restaurant.app.domain.model.Empleado

data class PersonalUiState (

    val empleados : List<Empleado> = emptyList(),
    val estaCargando : Boolean = false,
    val empleadoADarDeBaja : Empleado? = null // si no es null, se abre el AlertDialog de confirmacion
){

    //Total de empleados registrados para mostrar en el resumen superior.
    val totalEmpleados : Int get() = empleados.size
}