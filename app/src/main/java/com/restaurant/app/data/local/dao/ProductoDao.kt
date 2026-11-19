package com.restaurant.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.restaurant.app.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

/*
 * DAO (Data Access Object) PARA LA TABLA DE PRODUCTOS (Capa data/local).
 *
 * Define las operaciones SQL sobre la tabla 'productos':
 * - `observarTodos()`: Emite un Flow reactivo ordenado por nombre que se actualiza automáticamente ante altas, bajas o modificaciones.
 * - `guardar()`: Usa `@Upsert` (disponible desde Room 2.5+), que inserta si es un producto nuevo o actualiza si ya existe su ID.
 * - `eliminar()` y `obtenerPorId()`: Ejecutan consultas de baja y búsqueda directa.
 */
@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun observarTodos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: String): ProductoEntity?

    @Upsert
    suspend fun guardar(producto: ProductoEntity)

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun eliminar(id: String)

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contar(): Int
}
