package com.restaurant.app.data.local.mapper

import com.restaurant.app.data.local.entity.ProductoEntity
import com.restaurant.app.domain.model.Producto

/*
 * MAPPER DE PRODUCTO (Capa data/local).
 *
 * Permite desacoplar el modelo de dominio `Producto` de la entidad de Room `ProductoEntity`.
 * - `toDomain()`: Convierte de entidad de base de datos a objeto de negocio para la UI.
 * - `toEntity()`: Convierte de objeto de negocio a entidad de base de datos para guardar en SQLite.
 */

fun ProductoEntity.toDomain(): Producto {
    return Producto(
        id = id,
        nombre = nombre,
        categoria = categoria,
        area = area,
        precio = precio,
        estadoDisponible = estadoDisponible,
        estadoTexto = estadoTexto,
        descripcion = descripcion
    )
}

fun Producto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = id,
        nombre = nombre,
        categoria = categoria,
        area = area,
        precio = precio,
        estadoDisponible = estadoDisponible,
        estadoTexto = estadoTexto,
        descripcion = descripcion
    )
}
