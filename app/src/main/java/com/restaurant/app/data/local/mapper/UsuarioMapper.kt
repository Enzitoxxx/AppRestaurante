package com.restaurant.app.data.local.mapper

import com.restaurant.app.data.local.entity.UsuarioEntity
import com.restaurant.app.domain.model.Usuario

/*
 * MAPPER DE USUARIO (Capa data/local).
 *
 * Un Mapper es una clase utilitaria o conjunto de funciones de extensión que transforma modelos de una capa a otra.
 * - `toDomain()`: Convierte una entidad de base de datos (`UsuarioEntity`) en un modelo de negocio puro (`Usuario`),
 *   omitiendo datos sensibles como el hash de la contraseña.
 * - `toEntity(...)`: Convierte un modelo de dominio (`Usuario`) en una entidad de datos (`UsuarioEntity`),
 *   agregando el hash de la contraseña necesario para la persistencia en la base de datos local.
 * Esto mantiene la capa de dominio desvinculada de Room.
 */

fun UsuarioEntity.toDomain(): Usuario {
    return Usuario(
        id = id,
        nombre = nombre,
        correo = correo,
        telefono = telefono,
        estaActivo = estaActivo,
        restauranteNombre = restauranteNombre
    )
}

fun Usuario.toEntity(contrasenaHash: String): UsuarioEntity {
    return UsuarioEntity(
        id = id,
        nombre = nombre,
        correo = correo,
        telefono = telefono,
        restauranteNombre = restauranteNombre,
        estaActivo = estaActivo,
        contrasenaHash = contrasenaHash
    )
}
