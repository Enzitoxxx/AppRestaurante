package com.restaurant.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * ENTIDAD DE ROOM PARA PRODUCTOS (Capa data/local).
 *
 * Representa la tabla 'productos' en la base de datos SQLite local de Room.
 * Mantiene los mismos atributos que el modelo de dominio `Producto`, permitiendo
 * persistir la carta del restaurante (platos, bebidas, postres) de forma permanente en el dispositivo.
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
    val descripcion: String = ""
)
