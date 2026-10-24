package com.restaurant.app.domain.repository

import com.restaurant.app.domain.model.Empleado
import kotlinx.coroutines.flow.Flow

interface PersonalRepository {

    //Devuelve la lista de empleados y avisa cada vez que cambia
    fun  observarPersonal() : Flow<List<Empleado>>

    //Busca un empleado por ID
    suspend fun  obtenerEmpleado(id : String): Empleado?

    //Guarda el alta o edicion
    suspend fun guardarEmpleado(empleado: Empleado)

    //Eliminar / dar de baja
    suspend fun eliminarEmpleado(id: String)

}