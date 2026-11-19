package com.restaurant.app.data.repository

import com.restaurant.app.data.local.dao.UsuarioDao
import com.restaurant.app.data.local.entity.UsuarioEntity
import com.restaurant.app.data.local.mapper.toDomain
import com.restaurant.app.data.local.sesion.SesionPreferencias
import com.restaurant.app.domain.model.Usuario
import com.restaurant.app.domain.repository.AuthRepository
import com.restaurant.app.domain.repository.ResultadoLogin
import com.restaurant.app.domain.repository.ResultadoRegistro
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID

/*
 * IMPLEMENTACIÓN CONCRETA DEL REPOSITORIO DE AUTENTICACIÓN (Capa data/repository).
 *
 * Esta clase implementa la interfaz `AuthRepository` definida en la capa de dominio.
 * Se encarga de orquestar la fuente de datos local compuesta por:
 * 1. `UsuarioDao`: para la persistencia en la base de datos de Room.
 * 2. `SesionPreferencias`: para almacenar el usuario activo en SharedPreferences.
 *
 * Además, inicializa cuentas de prueba al abrir la aplicación por primera vez si la base de datos está vacía.
 */
class AuthRepositoryDatos(
    private val usuarioDao: UsuarioDao,
    private val sesionPreferencias: SesionPreferencias
) : AuthRepository {

    // Inicialización síncrona/perezosa de usuarios de prueba cuando la tabla está vacía
    private suspend fun asegurarUsuariosEjemplo() {
        if (usuarioDao.contar() == 0) {
            val usuarioActivo = UsuarioEntity(
                id = "1",
                nombre = "Lucía Fernández",
                correo = "lucia@labuenamesa.com.ar",
                telefono = "+54 9 11 1234-5678",
                restauranteNombre = "La Buena Mesa",
                estaActivo = true,
                contrasenaHash = UsuarioEntity.generarHash("Mesa1234")
            )
            val usuarioInactivo = UsuarioEntity(
                id = "2",
                nombre = "Usuario Inactivo",
                correo = "inactivo@labuenamesa.com.ar",
                telefono = "+54 9 11 0000-0000",
                restauranteNombre = "La Buena Mesa",
                estaActivo = false,
                contrasenaHash = UsuarioEntity.generarHash("Mesa1234")
            )
            try {
                usuarioDao.insertar(usuarioActivo)
                usuarioDao.insertar(usuarioInactivo)
            } catch (_: Exception) {
                // Si ocurre una colisión al sembrar datos, se ignora
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val usuarioActual: Flow<Usuario?> =
        sesionPreferencias.usuarioActualId.flatMapLatest { id ->
            if (id == null) {
                flowOf(null)
            } else {
                usuarioDao.observarPorId(id).map { entity ->
                    entity?.toDomain()
                }
            }
        }

    override suspend fun iniciarSesion(correo: String, contrasena: String): ResultadoLogin {
        asegurarUsuariosEjemplo()
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

        sesionPreferencias.guardarSesion(entity.id)
        return ResultadoLogin.Exito(entity.toDomain())
    }

    override suspend fun registrar(
        nombre: String,
        correo: String,
        telefono: String,
        restauranteNombre: String,
        contrasena: String
    ): ResultadoRegistro {
        asegurarUsuariosEjemplo()
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
            sesionPreferencias.guardarSesion(nuevoUsuario.id)
            ResultadoRegistro.Exito(nuevoUsuario.toDomain())
        } catch (_: Exception) {
            ResultadoRegistro.CorreoYaRegistrado
        }
    }

    override suspend fun recuperarContrasena(correo: String): Boolean {
        asegurarUsuariosEjemplo()
        val correoLimpio = correo.trim().lowercase()
        // Simulación: retorna true si el correo existe registrado en el sistema local
        return usuarioDao.buscarPorCorreo(correoLimpio) != null
    }

    override suspend fun cerrarSesion() {
        sesionPreferencias.cerrarSesion()
    }
}
