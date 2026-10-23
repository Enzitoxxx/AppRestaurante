package com.restaurant.app.domain.repository

import com.restaurant.app.domain.model.Mesa
import kotlinx.coroutines.flow.Flow

interface MesaRepository {

    // Devuelve la lista de mesas y AVISA cada vez que cambia
    fun observarMesas(): Flow<List<Mesa>>

    // Busca una mesa por id
    suspend fun obtenerMesa(id: String): Mesa?

    // Alta si es nueva, edición si ya existe
    suspend fun guardarMesa(mesa: Mesa)

    // Baja / Eliminar
    suspend fun eliminarMesa(id: String)
}
