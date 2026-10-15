package com.restaurant.app.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Componente visual reutilizable que representa el logotipo principal de la marca.
 *
 * Muestra un contenedor cuadrado con esquinas redondeadas y un ícono de cubiertos/restaurante
 * centrado en su interior.
 *
 * @param modifier Modificador de Compose para personalizar la posición, márgenes o comportamiento.
 * @param size Tamaño global (ancho y alto) del contenedor del logo. Por defecto es 80.dp.
 * @param cornerRadius Radio de curvatura aplicado a las esquinas del contenedor. Por defecto es 24.dp.
 */
@Composable
fun BrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    cornerRadius: Dp = 24.dp
) {
    // Contenedor Box que sirve de fondo estilizado para el logo
    Box(
        modifier = modifier
            .size(size) // Define dimensiones cuadradas
            .clip(RoundedCornerShape(cornerRadius)) // Corta los bordes con la curvatura indicada
            .background(MaterialTheme.colorScheme.primary), // Aplica el color primario de la marca (Terracota)
        contentAlignment = Alignment.Center // Alinea el ícono perfectamente al centro
    ) {
        // Ícono gráfico de la marca
        Icon(
            imageVector = Icons.Filled.Restaurant, // Ícono de cubiertos proveniente de Material Icons
            contentDescription = "Logo del Restaurante", // Texto de accesibilidad para lectores de pantalla
            tint = MaterialTheme.colorScheme.onPrimary, // Color blanco para contrastar con el fondo primario
            modifier = Modifier.size(size * 0.55f) // Escala el ícono al 55% del tamaño del contenedor
        )
    }
}