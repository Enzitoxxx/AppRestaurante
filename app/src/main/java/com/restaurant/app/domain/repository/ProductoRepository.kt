package com.restaurant.app.domain.repository

import com.restaurant.app.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {

    // Devuelve la lista y AVISA cada vez que cambia
    fun observarProductos(): Flow<List<Producto>>

    // Busca un producto por id (para precargar el formulario en modo edición)
    suspend fun obtenerProducto(id: String): Producto?

    // Alta si es nuevo, edición si ya existe
    suspend fun guardarProducto(producto: Producto)

    // Baja
    suspend fun eliminarProducto(id: String)
}