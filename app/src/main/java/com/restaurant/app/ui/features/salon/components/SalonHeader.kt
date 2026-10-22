package com.restaurant.app.ui.features.salon.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.restaurant.app.R

@Composable
fun SalonHeader(
    ocupadasCount: Int,
    libresCount: Int,
    cuentaCount: Int,
    zonaSeleccionada: String,
    onZonaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val zonas = listOf("Salón", "Patio")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = stringResource(R.string.salon_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(
                    R.string.salon_resumen_formato,
                    ocupadasCount,
                    libresCount,
                    cuentaCount
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Selector de Zona (Salón / Patio)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            zonas.forEach { zona ->
                val estaSeleccionado = zonaSeleccionada.equals(zona, ignoreCase = true)

                FilterChip(
                    selected = estaSeleccionado,
                    onClick = { onZonaSeleccionada(if (estaSeleccionado) "Todas" else zona) },
                    label = { Text(text = zona, style = MaterialTheme.typography.labelMedium) },
                    shape = RoundedCornerShape(16.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = null
                )
            }
        }
    }
}