package com.restaurant.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Esquema de colores semánticos en modo claro (Light Color Scheme) para Material Design 3.
 * Asocia las constantes numéricas de color a los roles visuales del sistema de diseño.
 */
private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = OnTerracottaPrimary,
    background = BackgroundWarm,
    surface = SurfaceWarm,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

/**
 * Tema principal de la aplicación.
 *
 * Envuelve los componentes Jetpack Compose inyectándoles la paleta de colores (colorScheme)
 * y los estilos de fuente (typography) definidos para la marca.
 *
 * @param darkTheme Determina si se aplica la variante oscura (por defecto según ajustes del sistema).
 * @param content Bloque de código Compose al que se le aplicará el tema.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Alias del tema principal con el nombre específico del proyecto (AppRestauranteTheme).
 * Garantiza compatibilidad en todo el proyecto.
 */
@Composable
fun AppRestauranteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AppTheme(darkTheme = darkTheme, content = content)
}