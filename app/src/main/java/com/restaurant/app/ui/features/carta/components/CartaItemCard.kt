package com.restaurant.app.ui.features.carta.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Icecream
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.restaurant.app.domain.model.Producto

@Composable
fun CartaItemCard(
    producto: Producto,
    onToggleDisponibilidad: (String) -> Unit,
    onEditarClick: (Producto) -> Unit,
    onBajaClick: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono con contenedor pastel según categoría
            val (icono, colorFondo, colorIcono) = obtenerEstiloCategoria(producto.categoria)

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorFondo),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            // Información del producto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(4.dp))

                val textoSubtitulo = if (producto.estadoTexto != null) {
                    "${producto.categoria} · ${producto.estadoTexto}"
                } else {
                    "${producto.categoria} · ${producto.area}"
                }

                Text(
                    text = textoSubtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (producto.estadoTexto != null) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(6.dp))

                val precioFormateado = try {
                    String.format(java.util.Locale.GERMANY, "%,d", producto.precio.toLong())
                } catch (e: Exception) {
                    producto.precio.toInt().toString()
                }

                Text(
                    text = "$ $precioFormateado",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Acciones: Switch + Botón Editar (Lápiz) + Botón Baja (Tacho)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Switch(
                    checked = producto.estadoDisponible,
                    onCheckedChange = { onToggleDisponibilidad(producto.id) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                Row {
                    // Botón Editar (Lápiz)
                    IconButton(
                        onClick = { onEditarClick(producto) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar producto",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Botón Baja Lógica (Tacho)
                    IconButton(
                        onClick = { onBajaClick(producto) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Dar de baja producto",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/** Retorna el icono, color de fondo pastel y color del icono según la categoría */
private fun obtenerEstiloCategoria(categoria: String): Triple<ImageVector, Color, Color> {
    return when {
        categoria.contains("Entrada", ignoreCase = true) -> Triple(
            Icons.Outlined.BakeryDining,
            Color(0xFFFFF3E0), // Naranja suave
            Color(0xFFE65100)
        )
        categoria.contains("Plato", ignoreCase = true) -> Triple(
            Icons.Outlined.Restaurant,
            Color(0xFFFFFDE7), // Amarillo suave
            Color(0xFFF57F17)
        )
        categoria.contains("Bebida", ignoreCase = true) -> Triple(
            Icons.Outlined.LocalBar,
            Color(0xFFFCE4EC), // Rosa suave
            Color(0xFFC2185B)
        )
        categoria.contains("Postre", ignoreCase = true) -> Triple(
            Icons.Outlined.Icecream,
            Color(0xE8E0F2F1), // Verde menta suave
            Color(0xFF00796B)
        )
        else -> Triple(
            Icons.Outlined.Fastfood,
            Color(0xFFF5F5F5),
            Color(0xFF616161)
        )
    }
}
