package com.restaurant.app

import android.app.Application
import com.restaurant.app.data.Repositorios

/*
 * CLASE DE APLICACIÓN PRINCIPAL (RestauranteApp).
 *
 * En Android, una clase que hereda de `Application` es el punto de entrada global de la app y se ejecuta
 * ANTES que cualquier pantalla (Activity/Composable).
 *
 * ¿Por qué hace falta esta clase?
 * La base de datos de Room y las SharedPreferences requieren un `Context` de Android para poder crear o abrir
 * los archivos en el almacenamiento del dispositivo. Al inicializar `Repositorios.iniciar(applicationContext)`
 * en el `onCreate()` de la Application, garantizamos que las instancias de Room y SharedPreferences
 * existan y estén listas de forma segura antes de que cualquier ViewModel intente usarlas.
 */
class RestauranteApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializamos los repositorios con el contexto global de la aplicación
        Repositorios.iniciar(this)
    }
}
