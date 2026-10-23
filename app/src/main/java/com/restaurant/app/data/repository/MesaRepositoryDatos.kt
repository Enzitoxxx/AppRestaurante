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
                numero = "Mesa 01",
                zona = "Salón",
                capacidadPersonas = 4,
                estado = EstadoMesa.LIBRE,
                detalleEstado = "Sin comanda"
            ),
            Mesa(
                id = "2",
                numero = "Mesa 02",
                zona = "Salón",
                capacidadPersonas = 2,
                estado = EstadoMesa.OCUPADA,
                mozoAsignado = "Camila G.",
                consumoTotal = 18200.0
            ),
            Mesa(
                id = "3",
                numero = "Mesa 03",
                zona = "Salón",
                capacidadPersonas = 4,
                estado = EstadoMesa.OCUPADA,
                mozoAsignado = "Camila G.",
                consumoTotal = 28600.0
            ),
            Mesa(
                id = "4",
                numero = "Mesa 04",
                zona = "Salón",
                capacidadPersonas = 4,
                estado = EstadoMesa.CUENTA,
                detalleEstado = "Esperando ticket",
                consumoTotal = 34100.0
            ),

            // Patio
            Mesa(
                id = "5",
                numero = "Mesa 05",
                zona = "Patio",
                capacidadPersonas = 2,
                estado = EstadoMesa.LIBRE
            ),
            Mesa(
                id = "6",
                numero = "Mesa 06",
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
}
