package com.restaurant.app.domain.repository

import com.restaurant.app.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

/*
 * Interfaz del repositorio de Autenticación (AuthRepository).
 *
 * Esta interfaz pertenece a la capa de dominio (domain), que es Kotlin puro.
 * No sabe de dónde provienen los datos (si vienen de Room, SharedPreferences, o en el futuro de Retrofit/API).
 * Define el "contrato" de todas las operaciones de autenticación que el sistema soporta.
 * La implementación concreta (AuthRepositoryDatos) vivirá en la capa data/repository.
 */
interface AuthRepository {

    // Emite en tiempo real el usuario que tiene la sesión activa en la app, o null si no hay nadie logueado.
    val usuarioActual: Flow<Usuario?>

    // Intenta iniciar sesión con correo y contraseña. Devuelve un ResultadoLogin indicando éxito o el tipo de error.
    suspend fun iniciarSesion(correo: String, contrasena: String): ResultadoLogin

    // Registra un nuevo usuario con los datos principales y contraseña.
    suspend fun registrar(
        nombre: String,
        correo: String,
        telefono: String,
        restauranteNombre: String,
        contrasena: String
    ): ResultadoRegistro

    // Simula la recuperación de contraseña comprobando si el correo existe en el sistema.
    suspend fun recuperarContrasena(correo: String): Boolean

    // Cierra la sesión activa limpiando los datos del usuario actual.
    suspend fun cerrarSesion()
}

/*
 * Representa los posibles resultados de un intento de inicio de sesión.
 * Se usa una `sealed interface` para garantizar exhaustividad en las expresiones `when` de la capa de presentación (ViewModel).
 */
sealed interface ResultadoLogin {
    data class Exito(val usuario: Usuario) : ResultadoLogin
    data object CredencialesInvalidas : ResultadoLogin
    data object CuentaInactiva : ResultadoLogin
}

/*
 * Representa los posibles resultados del registro de un nuevo usuario.
 */
sealed interface ResultadoRegistro {
    data class Exito(val usuario: Usuario) : ResultadoRegistro
    data object CorreoYaRegistrado : ResultadoRegistro
}
