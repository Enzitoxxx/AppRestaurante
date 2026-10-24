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

/*
 * TARJETA SELECCIONABLE DE MESA .
 *
 * Si la mesa ya la tiene otro camarero (asignadaA != null), la tarjeta queda
 * bloqueada y muestra "Asignada a (xxx).", para que no se pueda marcar.
 */
@Composable
fun MesaSeleccionableCard(
    mesa: Mesa,
    estaSeleccionada: Boolean,
    onToggleSeleccion: (String) -> Unit,
    asignadaA: String? = null,
    modifier: Modifier = Modifier
) {
    val bloqueada = asignadaA != null
    val colorBorde = if (estaSeleccionada) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outlineVariant

    Card(
        onClick = { onToggleSeleccion(mesa.id) },
        enabled = !bloqueada,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${mesa.nombre} · ${mesa.zona}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (bloqueada) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.asignacion_capacidad_formato, mesa.capacidadPersonas),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (asignadaA != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.asignacion_asignada_a_formato, asignadaA),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Checkbox(
                checked = estaSeleccionada,
                onCheckedChange = { onToggleSeleccion(mesa.id) },
                enabled = !bloqueada,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}