package com.restaurant.app.ui.features.comanda.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.restaurant.app.domain.model.ComandaModel
import com.restaurant.app.domain.model.EstadoComanda
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ComandaCard(
    comanda: ComandaModel,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "AR")).apply {
        maximumFractionDigits = 0
    }

    // Colores del Badge de Estado
    val (badgeBg, badgeText, estadoTexto) = when (comanda.estado) {
        EstadoComanda.EN_PREPARACION -> Triple(
            Color(0xFFFFF8E1), // Amarillo soft
            Color(0xFFD87D00), // Texto amarillo/naranja
            stringResource(R.string.comandas_estado_en_preparacion)
        )
        EstadoComanda.ENTREGADO -> Triple(
            Color(0xFFE8F5E9), // Verde soft
            Color(0xFF2E7D32), // Texto verde
            stringResource(R.string.comandas_estado_entregado)
        )
        EstadoComanda.COBRADO -> Triple(
            Color(0xFFE3F2FD), // Azul soft
            Color(0xFF1565C0), // Texto azul
            stringResource(R.string.comandas_estado_cobrado)
        )
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila Superior: Título Pedido/Mesa + Badge Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.comandas_pedido_mesa, comanda.id, comanda.numeroMesa),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Pill / Badge del Estado
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(badgeBg)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = estadoTexto,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = badgeText
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Subtítulo: Camarero/a
            Text(
                text = stringResource(R.string.comandas_camarero, comanda.camareroNombre),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(14.dp))

            // Fila Inferior: Cantidad items / Hora + Monto Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.comandas_items_hora, comanda.cantidadItems, comanda.hora),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = currencyFormatter.format(comanda.total),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}