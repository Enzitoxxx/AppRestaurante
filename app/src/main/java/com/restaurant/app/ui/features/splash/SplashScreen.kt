package com.restaurant.app.ui.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.restaurant.app.core.designsystem.BrandLogo
import com.restaurant.app.ui.theme.AppTheme
import com.restaurant.app.ui.theme.BrandGreen
import com.restaurant.app.ui.theme.TextSecondary
import com.restaurant.app.ui.theme.TextTertiary

/**
 * Pantalla de Bienvenida de la aplicación.
 *
 * Muestra la identidad de la marca cuando el usuario abre la aplicación:
 * - Logotipo central animado o estático (BrandLogo)
 * - Título del sistema ("MesaPro")
 * - Eslogan principal y mensaje secundario
 * - Pie de página informativo con sello ecológico y versión de la app
 *
 * @param modifier Modificador para personalizar la disposición de la pantalla.
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    // Contenedor principal a pantalla completa con fondo de color cálido
    Box(
        modifier = modifier
            .fillMaxSize() // Ocupa todo el ancho y alto disponible en pantalla
            .background(MaterialTheme.colorScheme.background) // Fondo cálido configurado en el tema
            .padding(horizontal = 32.dp, vertical = 24.dp) // Relleno interno para separación respecto a los bordes
    ) {
        // Columna central: Contiene el Logo, Título y Eslogan
        Column(
            modifier = Modifier.align(Alignment.Center), // Se posiciona justo al centro de la pantalla
            horizontalAlignment = Alignment.CenterHorizontally // Alinea todos los elementos hijos al centro horizontal
        ) {
            // Logotipo de la marca con tamaño personalizado
            BrandLogo(size = 88.dp, cornerRadius = 26.dp)

            // Espaciador vertical
            Spacer(modifier = Modifier.height(24.dp))

            // Nombre principal del producto/aplicación
            Text(
                text = "MesaPro", // Nombre de la aplicación. Lo podemos modificar a nuestro gusto
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Espaciador vertical entre el título y el subtítulo
            Spacer(modifier = Modifier.height(12.dp))

            // Subtítulo principal destacando la propuesta de valor
            Text(
                text = "Mi local, está en buenas manos.",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            // Eslogan secundario explicativo de dos líneas
            Text(
                text = "Más tiempo para lo que importa.\nEl sabor de hacer las cosas bien.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary, // Color gris medio configurado en el tema
                lineHeight = 20.sp, // Interlineado cómodo para lectura
                textAlign = TextAlign.Center
            )
        }

        // Columna inferior (Pie de página / Footer): Muestra información institucional
        Column(
            modifier = Modifier.align(Alignment.BottomCenter), // Se alinea en la parte inferior centrada
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp) // Espaciado constante de 6dp entre sus elementos
        ) {
            // Ícono de hojita/ecológico verde característico de la marca
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = "Sello Ecológico",
                tint = BrandGreen, // Color verde verde-hoja definido en la paleta
                modifier = Modifier.size(20.dp)
            )

            // Sello/Lema del destinatario del producto
            Text(
                text = "HECHO PARA MI RESTAURANTE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp, // Espaciado amplio entre letras para estilo de etiqueta
                color = TextTertiary // Color gris claro suave
            )
        }
    }
}

/**
 * Vista previa de Compose para visualizar el diseño de la SplashScreen en el editor de Android Studio.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    AppTheme {
        SplashScreen()
    }
}