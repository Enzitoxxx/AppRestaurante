package com.restaurant.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.restaurant.app.data.Repositorios
import com.restaurant.app.ui.navigation.AppNavHost
import com.restaurant.app.ui.theme.AppTheme

/*
 * Actividad principal (Activity) y punto de entrada de la aplicación.
 *
 * Responsabilidades:
 * - Iniciar el Service Locator `Repositorios` pasando el Context necesario para Room.
 * - Iniciar la interfaz gráfica mediante Jetpack Compose (setContent).
 * - Activar la experiencia Edge-to-Edge para aprovechar toda la pantalla.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * INICIALIZACIÓN DE REPOSITORIOS Y BASE DE DATOS:
         * Room requiere un `Context` de Android para crear la base de datos SQLite en almacenamiento local.
         * Inicializamos `Repositorios` aquí en `onCreate` antes de construir la interfaz gráfica.
         */
        Repositorios.iniciar(applicationContext)

        // Habilita que el contenido dibuje por debajo de las barras de estado y navegación transparentes
        enableEdgeToEdge()

        // Asigna la jerarquía de composables como contenido principal de la pantalla
        setContent {
            AppTheme {
                AppNavHost()
            }
        }
    }
}
