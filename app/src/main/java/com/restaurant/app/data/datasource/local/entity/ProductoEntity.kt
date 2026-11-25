package com.restaurant.app.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.restaurant.app.domain.model.Producto

/*
 * ENTIDAD DE ROOM PARA PRODUCTOS (Capa data/datasource/local/entity).
 *
 * `toDomain()` y `fromDomain()`:
 * La Entity representa la fila de la tabla en SQLite; el modelo de domain (`Producto`) es el que usa la app.
 * Estas funciones traducen entre ambos modelos para mantener la capa de dominio libre de dependencias de Room.
 */
@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val categoria: String,
    val area: String,
    val precio: Double,
    val estadoDisponible: Boolean,
    val estadoTexto: String? = null,
    val descripcion: String = "",
    val estaDadoDeBaja: Boolean = false
) {
    /**
     * Convierte la entidad de base de datos en el modelo de dominio `Producto`.
     */
    fun toDomain(): Producto {
        return Producto(
            id = id,
            nombre = nombre,
            categoria = categoria,
            area = area,
            precio = precio,
            estadoDisponible = estadoDisponible,
            estadoTexto = estadoTexto,
            descripcion = descripcion,
            estaDadoDeBaja = estaDadoDeBaja
        )
    }

    companion object {
        /**
         * Convierte un objeto de dominio `Producto` en una entidad de Room `ProductoEntity`.
         */
        fun fromDomain(producto: Producto): ProductoEntity {
            return ProductoEntity(
                id = producto.id,
                nombre = producto.nombre,
                categoria = producto.categoria,
                area = producto.area,
                precio = producto.precio,
                estadoDisponible = producto.estadoDisponible,
                estadoTexto = producto.estadoTexto,
                descripcion = producto.descripcion,
                estaDadoDeBaja = producto.estaDadoDeBaja
            )
        }
    }
}
