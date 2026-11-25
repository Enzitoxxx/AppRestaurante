package com.restaurant.app.data.repository

import com.restaurant.app.data.datasource.local.dao.UsuarioDao
import com.restaurant.app.data.datasource.local.entity.UsuarioEntity
import com.restaurant.app.domain.model.Usuario
import com.restaurant.app.domain.repository.AuthRepository
import com.restaurant.app.domain.repository.ResultadoLogin
import com.restaurant.app.domain.repository.ResultadoRegistro
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID

/*
 * IMPLEMENTACIÓN CONCRETA DEL REPOSITORIO DE AUTENTICACIÓN (Capa data/repository).
 *
 * GESTIÓN DE SESIÓN EN MEMORIA:
 * La sesión del usuario se mantiene activa en memoria mediante `usuarioActualId` (StateFlow) mientras la app está abierta.
 * Los usuarios iniciales se cargan automáticamente desde `DatosIniciales.kt` mediante el callback `onCreate` de Room.
 */
class AuthRepositoryDatos(
    private val usuarioDao: UsuarioDao
) : AuthRepository {

    // Guarda la ID del usuario que inició sesión en la corrida actual de la aplicación
    private val usuarioActualId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val usuarioActual: Flow<Usuario?> =
        usuarioActualId.flatMapLatest { id ->
            if (id == null) {
                flowOf(null)
            } else {
                usuarioDao.observarPorId(id).map { entity ->
                    entity?.toDomain()
                }
            }
        }

    override suspend fun iniciarSesion(correo: String, contrasena: String): ResultadoLogin {
        val correoLimpio = correo.trim().lowercase()
        val entity = usuarioDao.buscarPorCorreo(correoLimpio)

        if (entity == null) {
            return ResultadoLogin.CredencialesInvalidas
        }

        val hashIngresado = UsuarioEntity.generarHash(contrasena)
        if (entity.contrasenaHash != hashIngresado) {
            return ResultadoLogin.CredencialesInvalidas
        }

        if (!entity.estaActivo) {
            return ResultadoLogin.CuentaInactiva
        }

        usuarioActualId.value = entity.id
        return ResultadoLogin.Exito(entity.toDomain())
    }

    override suspend fun registrar(
        nombre: String,
        correo: String,
        telefono: String,
        restauranteNombre: String,
        contrasena: String
    ): ResultadoRegistro {
        val correoLimpio = correo.trim().lowercase()

        val usuarioExistente = usuarioDao.buscarPorCorreo(correoLimpio)
        if (usuarioExistente != null) {
            return ResultadoRegistro.CorreoYaRegistrado
        }

        val nuevoUsuario = UsuarioEntity(
            id = UUID.randomUUID().toString(),
            nombre = nombre.trim(),
            correo = correoLimpio,
            telefono = telefono.trim(),
            restauranteNombre = restauranteNombre.trim(),
            estaActivo = true,
            contrasenaHash = UsuarioEntity.generarHash(contrasena)
        )

        return try {
            usuarioDao.insertar(nuevoUsuario)
            usuarioActualId.value = nuevoUsuario.id
            ResultadoRegistro.Exito(nuevoUsuario.toDomain())
        } catch (_: Exception) {
            ResultadoRegistro.CorreoYaRegistrado
        }
    }

    override suspend fun recuperarContrasena(correo: String): Boolean {
        val correoLimpio = correo.trim().lowercase()
        return usuarioDao.buscarPorCorreo(correoLimpio) != null
    }

    override suspend fun cerrarSesion() {
        usuarioActualId.value = null
    }
}
