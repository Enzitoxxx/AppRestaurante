package com.restaurant.app.ui.features.salon.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.restaurant.app.R
import com.restaurant.app.data.model.EstadoMesa
import com.restaurant.app.data.model.Mesa

@Composable
fun MesaCard(
    mesa: Mesa,
    onMesaClick: (Mesa) -> Unit,
    modifier: Modifier = Modifier
) {
    val (colorBorde, colorFondoCard, colorBadgeFondo, textoBadge) = obtenerEstiloMesa(mesa.estado)

    Card(
        onClick = { onMesaClick(mesa) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondoCard),
        border = BorderStroke(1.5.dp, colorBorde),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Fila Superior: Nombre de la Mesa + Badge de Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mesa.numero,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(colorBadgeFondo)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = textoBadge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Fila Inferior: Detalles (Personas, Mozo, Total de consumo)
            when (mesa.estado) {
                EstadoMesa.LIBRE -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = "${mesa.capacidadPersonas} personas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (mesa.detalleEstado != null) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = mesa.detalleEstado,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                EstadoMesa.OCUPADA -> {
                    if (mesa.mozoAsignado != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = mesa.mozoAsignado,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Text(
                            text = "${mesa.capacidadPersonas} personas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (mesa.consumoTotal != null) {
                        Spacer(Modifier.height(4.dp))
                        val precioFormateado = String.format(java.util.Locale.GERMANY, "%,d", mesa.consumoTotal.toLong())
                        Text(
                            text = "$ $precioFormateado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                EstadoMesa.CUENTA -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.ReceiptLong,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = mesa.detalleEstado ?: "Esperando ticket",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD97706)
                        )
                    }

                    if (mesa.consumoTotal != null) {
                        Spacer(Modifier.height(4.dp))
                        val precioFormateado = String.format(java.util.Locale.GERMANY, "%,d", mesa.consumoTotal.toLong())
                        Text(
                            text = "$ $precioFormateado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD97706)
                        )
                    }
                }
            }
        }
    }
}

/** Retorna (ColorBorde, ColorFondoCard, ColorBadgeFondo, TextoBadge) según el estado de la mesa */
@Composable
private fun obtenerEstiloMesa(estado: EstadoMesa): Quadruple<Color, Color, Color, String> {
    return when (estado) {
        EstadoMesa.LIBRE -> Quadruple(
            Color(0xFF2EC4B6),                   // Borde verde
            Color(0xFFE8F9F8),                   // Fondo verde muy suave
            Color(0xFF2EC4B6),                   // Badge verde
            stringResource(R.string.salon_estado_libre)
        )
        EstadoMesa.OCUPADA -> Quadruple(
            Color(0xFFFF6B6B),                   // Borde rojo/rosa
            Color(0xFFFFF0F0),                   // Fondo rosa muy suave
            Color(0xFFFF6B6B),                   // Badge rojo
            stringResource(R.string.salon_estado_ocupada)
        )
        EstadoMesa.CUENTA -> Quadruple(
            Color(0xFFF59E0B),                   // Borde amarillo/naranja
            Color(0xFFFFFBEB),                   // Fondo amarillo suave
            Color(0xFFF59E0B),                   // Badge amarillo
            stringResource(R.string.salon_estado_cuenta)
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)