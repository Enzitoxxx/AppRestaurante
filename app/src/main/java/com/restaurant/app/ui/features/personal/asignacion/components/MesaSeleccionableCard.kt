package com.restaurant.app.ui.features.personal.asignacion.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.restaurant.app.R
import com.restaurant.app.domain.model.Mesa

/**
 * TARJETA SELECCIONABLE DE MESA CON CHECKBOX.
 *
 * Muestra el número de mesa y su zona (ej: "Mesa 01 · Salón"), la capacidad
 * y un Checkbox naranja a la derecha para marcar o desmarcar la asignación.
 */
@Composable
fun MesaSeleccionableCard(
    mesa: Mesa,
    estaSeleccionada: Boolean,
    onToggleSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorBorde = if (estaSeleccionada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant

    Card(
        onClick = { onToggleSeleccion(mesa.id) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, colorBorde),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información de la Mesa (Número, Zona y Capacidad)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${mesa.numero} · ${mesa.zona}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.asignacion_capacidad_formato, mesa.capacidadPersonas),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Checkbox Naranja de selección
            Checkbox(
                checked = estaSeleccionada,
                onCheckedChange = { onToggleSeleccion(mesa.id) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}
