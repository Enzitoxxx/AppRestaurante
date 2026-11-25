package com.restaurant.app.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.restaurant.app.data.datasource.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

/*
 * DAO (Data Access Object) PARA LA TABLA DE PRODUCTOS (Capa data/datasource/local/dao).
 *
 * Define las operaciones SQL sobre la tabla 'productos':
 * - `observarTodos()`: Emite solo los productos activos (estaDadoDeBaja = 0) para no mostrarlos en la Carta.
 * - `obtenerPorId()`: Devuelve cualquier producto por ID aunque esté dado de baja (para historial de pedidos).
 * - `guardar()`: Usa `@Upsert` para altas y ediciones.
 * - `eliminar()`: Ejecuta la baja física cuando el producto no tiene historial.
 */
@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos WHERE estaDadoDeBaja = 0 ORDER BY nombre ASC")
    fun observarTodos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: String): ProductoEntity?

    @Upsert
    suspend fun guardar(producto: ProductoEntity)

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun eliminar(id: String)
}
