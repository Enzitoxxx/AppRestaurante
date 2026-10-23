package com.restaurant.app.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Chair
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.restaurant.app.R

/** StatusMetricsRow = Fila de indicadores de estado.
 MetricCard = Tarjeta de indicador.

 StatusMetricsRow: Es la funcion principal. Se encarga de colocar las dos tarjetas
 una al lado de la otra
 Crea una fila que ocupa tod el ancho disponible y dentro de esa fila
 llama dos veces a "MetricCard()"
 La primera llamada crea la tarjeta de los pedidos
 La segunda llamada crea la tarjeta de las mesas

 MetricCard: Es la funcion que construye visualmente la tarjeta.
 Esta es la tarjeta que llamamos en cualquier lado para crear una digamos.

 */

@Composable
fun StatusMetricsRow(
    pedidosAbiertos: Int,
    mesasOcupadas: Int,
    totalMesas: Int,
    porcentajeOcupacion: Int,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            iconBoxBg = MaterialTheme.colorScheme.primaryContainer,
            iconTint = MaterialTheme.colorScheme.primary,
            icon = Icons.Outlined.Assignment,
            label = stringResource(R.string.home_open_orders_label),
            value = "$pedidosAbiertos activos",
            subtitle = stringResource(R.string.home_open_orders_subtitle)
        )

        MetricCard(
            modifier = Modifier.weight(1f),
            iconBoxBg = MaterialTheme.colorScheme.secondaryContainer,
            iconTint = MaterialTheme.colorScheme.secondary,
            icon = Icons.Outlined.Chair,
            label = stringResource(R.string.home_occupied_tables_label),
            value = "$mesasOcupadas / $totalMesas",
            subtitle = stringResource(R.string.home_occupancy_subtitle, porcentajeOcupacion)
        )
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    iconBoxBg: Color,
    iconTint: Color,
    icon: ImageVector,
    label: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = MaterialTheme.shapes.medium
        ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(iconBoxBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}