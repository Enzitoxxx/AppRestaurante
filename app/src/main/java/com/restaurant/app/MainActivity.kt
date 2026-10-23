package com.restaurant.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.restaurant.app.ui.features.splash.SplashScreen
import com.restaurant.app.ui.navigation.AppNavHost
import com.restaurant.app.ui.theme.AppTheme

/*
 * Actividad principal (Activity) y punto de entrada de la aplicación.
 *
 * Responsabilidades:
 * - Iniciar la interfaz gráfica mediante Jetpack Compose (setContent).
 * - Activar la experiencia Edge-to-Edge para aprovechar toda la pantalla.
 * - Aplicar el tema global de la app (AppTheme) e inflar la pantalla inicial (SplashScreen).
 */
class MainActivity : ComponentActivity() {

    /*
     * Método de ciclo de vida invocado cuando la actividad se inicia por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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