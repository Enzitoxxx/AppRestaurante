package com.restaurant.app.data.local.sesion

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
 * MANEJADOR DE PREFERENCIAS DE SESIÓN (Capa data/local).
 *

 * ¿Por qué usamos SharedPreferences para la sesión y no Room?
 * Room es una base de datos relacional orientada a almacenar colecciones de datos complejas (como listas de usuarios o productos).
 * Para saber qué usuario tiene la sesión iniciada en la aplicación, solo necesitamos guardar una clave simple:
 * la ID del usuario actual (o null si la sesión se cerró). SharedPreferences es liviano, rápido y persistente en el dispositivo.
 * Exponemos `usuarioActualId` como un StateFlow para que la UI o los repositorios reaccionen en tiempo real ante el login/logout.
 */
class SesionPreferencias(context: Context) {

    private val pref = context.getSharedPreferences("sesion_restaurante_prefs", Context.MODE_PRIVATE)

    private val _usuarioActualId = MutableStateFlow<String?>(
        pref.getString(CLAVE_USUARIO_ID, null)
    )
    val usuarioActualId: StateFlow<String?> = _usuarioActualId.asStateFlow()

    /**
     * Guarda la ID del usuario autenticado en disco y actualiza el StateFlow reactivo.
     */
    fun guardarSesion(usuarioId: String) {
        pref.edit().putString(CLAVE_USUARIO_ID, usuarioId).apply()
        _usuarioActualId.value = usuarioId
    }

    /**
     * Elimina la ID del usuario de SharedPreferences al cerrar sesión.
     */
    fun cerrarSesion() {
        pref.edit().remove(CLAVE_USUARIO_ID).apply()
        _usuarioActualId.value = null
    }

    private companion object {
        private const val CLAVE_USUARIO_ID = "usuario_activo_id"
    }
}
