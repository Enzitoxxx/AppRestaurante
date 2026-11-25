package com.restaurant.app.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.restaurant.app.domain.model.Usuario
import java.security.MessageDigest

/*
 * ENTIDAD DE ROOM PARA USUARIOS (Capa data/datasource/local/entity).
 *
 * `toDomain()` y `fromDomain()`:
 * La Entity representa la fila de la tabla en SQLite; el modelo de domain (`Usuario`) es el que usa la app.
 * Estas funciones traducen entre ambos modelos para mantener la capa de dominio libre de dependencias de Room.
 */
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val restauranteNombre: String,
    val estaActivo: Boolean,
    val contrasenaHash: String
) {
    /**
     * Convierte la entidad de la base de datos en un objeto de dominio para la app,
     * omitiendo datos sensibles de la capa de datos como la contraseña.
     */
    fun toDomain(): Usuario {
        return Usuario(
            id = id,
            nombre = nombre,
            correo = correo,
            telefono = telefono,
            estaActivo = estaActivo,
            restauranteNombre = restauranteNombre
        )
    }

    companion object {
        private const val SAL_SECRETA = "AppRestaurante_Sal_2026"

        /**
         * Convierte un objeto de dominio `Usuario` en una entidad de Room `UsuarioEntity`,
         * asociando el hash de la contraseña para su almacenamiento en SQLite.
         */
        fun fromDomain(usuario: Usuario, contrasenaHash: String): UsuarioEntity {
            return UsuarioEntity(
                id = usuario.id,
                nombre = usuario.nombre,
                correo = usuario.correo,
                telefono = usuario.telefono,
                restauranteNombre = usuario.restauranteNombre,
                estaActivo = usuario.estaActivo,
                contrasenaHash = contrasenaHash
            )
        }

        /**
         * Genera un hash SHA-256 seguro a partir de una contraseña ingresada por el usuario.
         */
        fun generarHash(contrasena: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val textoConSal = contrasena + SAL_SECRETA
            val hashBytes = digest.digest(textoConSal.toByteArray(Charsets.UTF_8))
            return hashBytes.joinToString("") { "%02x".format(it) }
        }
    }
}
