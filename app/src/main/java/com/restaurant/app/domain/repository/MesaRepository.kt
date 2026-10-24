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

    // Asigna al empleado EXACTAMENTE estas mesas. Las que tenía y no están en la lista
    // quedan sin asignar, y las que tenía otro camarero pasan a este (una mesa, un camarero).
    suspend fun asignarMesas(empleadoId: String, idsMesas: Set<String>)

    // Le quita al empleado las mesas que NO estén en la zona permitida.
    // Si zonaPermitida es null, le quita todas.
    suspend fun liberarMesasFueraDeZona(empleadoId: String, zonaPermitida: String?)
}
