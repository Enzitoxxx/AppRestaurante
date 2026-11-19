package com.restaurant.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.security.MessageDigest

/*
 * ENTIDAD DE ROOM PARA USUARIOS (Capa data/local).
 *
 * 1. ¿Por qué una Entity separada del modelo de Domain?
 *    En la capa de dominio (domain/model/Usuario.kt) el modelo no incluye contraseña por motivos
 *    de seguridad y buenas prácticas de arquitectura. La entidad de Room vive únicamente en
 *    la capa de datos local y es la encargada de estructurar la tabla 'usuarios' de SQLite.
 *
 * 2. Hash de contraseña:
 *    La contraseña NUNCA se guarda en texto plano. Se calcula un hash SHA-256 concatenando
 *    una "sal" (salt) estática para proteger contra ataques de tablas arcoíris.
 *    En una aplicación real de producción, el hashing y la sal individual los procesa
 *    el backend/servidor usando algoritmos robustos como bcrypt o Argon2.
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
    companion object {
        // Sal simple estática para ocultar la contraseña en el hash
        private const val SAL_SECRETA = "AppRestaurante_Sal_2026"

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
