package com.restaurant.app.ui.features.personal.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.restaurant.app.domain.model.Empleado

@Composable
fun EmpleadoCard(
    empleado: Empleado,
    onEditarClick: (Empleado) -> Unit = {},
    onAsignarMesasClick: (Empleado) -> Unit = {},
    onBajaClick: (Empleado) -> Unit = {},
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
            // Círculo con las iniciales
            val (colorFondoAvatar, colorTextoAvatar) = obtenerColorAvatar(empleado.nombre)

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(colorFondoAvatar),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = empleado.iniciales,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorTextoAvatar
                )
            }
            Spacer(Modifier.width(14.dp))

            // Información del empleado (nombre, rol y asignación)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = empleado.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${empleado.rol} · ${empleado.turno} (${empleado.asignacion})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Badge de Estado (Activo / Inactivo) y botones de acción
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Badge activo / inactivo
                val (colorBadgeFondo, colorBadgeTexto, textBadge) = if (empleado.estaActivo) {
                    Triple(Color(0XFFE8F5E9), Color(0xFF2E7D32), stringResource(R.string.personal_estado_activo))
                } else {
                    Triple(Color(0xFFF5F5F5), Color(0xFF616161), stringResource(R.string.personal_estado_inactivo))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorBadgeFondo)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = textBadge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colorBadgeTexto
                    )
                }

                // Botones: Silla (Asignar Mesas), Lápiz (Editar Datos) y Tacho (Baja)
                Row {
                    // Muestra el icono de Silla 🪑 solo si el rol es Camarero/Mozo
                    val esCamarero = empleado.rol.contains("Camarer", ignoreCase = true) ||
                            empleado.rol.contains("Mozo", ignoreCase = true)

                    if (esCamarero) {
                        IconButton(
                            onClick = { onAsignarMesasClick(empleado) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Chair,
                                contentDescription = "Asignar mesas",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Botón Editar Datos del Empleado ✏️
                    IconButton(
                        onClick = { onEditarClick(empleado) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar datos empleado",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Botón Baja Lógica 🗑️
                    IconButton(
                        onClick = { onBajaClick(empleado) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Dar de baja empleado",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun obtenerColorAvatar(nombre: String): Pair<Color, Color> {
    val hash = nombre.hashCode()
    return when (Math.abs(hash) % 3) {
        0 -> Pair(Color(0xFFFFF3E0), Color(0xFFE65100)) // Naranja pastel
        1 -> Pair(Color(0xFFFFFDE7), Color(0xFFF57F17)) // Amarillo pastel
        else -> Pair(Color(0xFFECEFF1), Color(0xFF455A64)) // Gris pastel
    }
}
