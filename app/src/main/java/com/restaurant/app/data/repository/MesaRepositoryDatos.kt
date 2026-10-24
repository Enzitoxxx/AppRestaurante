package com.restaurant.app.data.repository

import com.restaurant.app.domain.model.EstadoMesa
import com.restaurant.app.domain.model.Mesa
import com.restaurant.app.domain.repository.MesaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MesaRepositoryDatos : MesaRepository {

    // La lista viva de mesas con StateFlow
    private val mesas = MutableStateFlow(
        listOf(
            // Salón
            Mesa(
                id = "1",
                numero = 1,
                zona = "Salon",
                capacidadPersonas = 4,
                estado = EstadoMesa.LIBRE,
                detalleEstado = "Sin comanda"
            ),
            Mesa(
                id = "2",
                numero = 2,
                zona = "Salon",
                capacidadPersonas = 2,
                estado = EstadoMesa.OCUPADA,
                empleadoAsignadoId = "2",
                consumoTotal = 18200.0
            ),
            Mesa(
                id = "3",
                numero = 3,
                zona = "Salon",
                capacidadPersonas = 4,
                estado = EstadoMesa.OCUPADA,
                empleadoAsignadoId = "2",
                consumoTotal = 28600.0
            ),
            Mesa(
                id = "4",
                numero = 4,
                zona = "Salon",
                capacidadPersonas = 4,
                estado = EstadoMesa.CUENTA,
                detalleEstado = "Esperando ticket",
                consumoTotal = 34100.0
            ),

            // Patio
            Mesa(
                id = "5",
                numero = 5,
                zona = "Patio",
                capacidadPersonas = 2,
                estado = EstadoMesa.LIBRE
            ),
            Mesa(
                id = "6",
                numero = 6,
                zona = "Patio",
                capacidadPersonas = 6,
                estado = EstadoMesa.OCUPADA,
                consumoTotal = 42000.0
            )
        )
    )

    override fun observarMesas(): Flow<List<Mesa>> = mesas.asStateFlow()

    override suspend fun obtenerMesa(id: String): Mesa? =
        mesas.value.find { it.id == id }

    override suspend fun guardarMesa(mesa: Mesa) {
        mesas.update { lista ->
            if (lista.any { it.id == mesa.id }) {
                lista.map { if (it.id == mesa.id) mesa else it }
            } else {
                lista + mesa
            }
        }
    }

    override suspend fun eliminarMesa(id: String) {
        mesas.update { lista -> lista.filterNot { it.id == id } }
    }

    override suspend fun asignarMesas(empleadoId: String, idsMesas: Set<String>) {
        mesas.update { lista ->
            lista.map { mesa ->
                when {
                    mesa.id in idsMesas -> mesa.copy(empleadoAsignadoId = empleadoId)  // queda con este camarero
                    mesa.empleadoAsignadoId == empleadoId -> mesa.copy(empleadoAsignadoId = null)  // se la sacamos
                    else -> mesa                                                        // no se toca
                }
            }
        }
    }

    override suspend fun liberarMesasFueraDeZona(empleadoId: String, zonaPermitida: String?) {
        mesas.update { lista ->
            lista.map { mesa ->
                val esDeEsteEmpleado = mesa.empleadoAsignadoId == empleadoId
                val fueraDeZona = zonaPermitida == null || !mesa.zona.equals(zonaPermitida, ignoreCase = true)
                if (esDeEsteEmpleado && fueraDeZona) mesa.copy(empleadoAsignadoId = null) else mesa
            }
        }
    }
}