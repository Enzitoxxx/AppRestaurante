package com.restaurant.app.data.repository

import com.restaurant.app.domain.model.Empleado
import com.restaurant.app.domain.repository.PersonalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PersonalRepositoryDatos : PersonalRepository {

    private val empleados = MutableStateFlow(
        listOf(
            Empleado(
                id = "1",
                nombre = "Lisandro Aráoz",
                rol = "Cocinero",
                turno = "Turno Mañana",
                asignacion = "Cocina",
                estaActivo = true,
                telefono = "+54 9 381 555-0101",
                correo = "lisandro@labuenamesa.com.ar"
            ),
            Empleado(
                id = "2",
                nombre = "Camila Gómez",
                rol = "Camarero / Mozo",
                turno = "Turno Noche",
                asignacion = "Salón Principal",
                estaActivo = true,
                telefono = "+54 9 11 2345-6789",
                correo = "camila@labuenamesa.com.ar"
            ),
            Empleado(
                id = "3",
                nombre = "Carlos Paz",
                rol = "Bartender",
                turno = "Turno Noche",
                asignacion = "Barra",
                estaActivo = false,
                telefono = "+54 9 381 555-0103",
                correo = "carlos@labuenamesa.com.ar"
            )
        )
    )

    override fun observarPersonal(): Flow<List<Empleado>> = empleados.asStateFlow()

    override suspend fun obtenerEmpleado(id: String): Empleado? =
        empleados.value.find { it.id == id }

    override suspend fun guardarEmpleado(empleado: Empleado) {
        empleados.update { lista ->
            if (lista.any { it.id == empleado.id }) {
                lista.map { if (it.id == empleado.id) empleado else it }
            } else {
                lista + empleado
            }
        }
    }

    override suspend fun eliminarEmpleado(id: String) {
        empleados.update { lista -> lista.filterNot { it.id == id } }
    }
}