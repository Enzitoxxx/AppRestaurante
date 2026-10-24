package com.restaurant.app.ui.features.personal.asignacion.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.restaurant.app.R

/**
 * ENCABEZADO DE LA PANTALLA ASIGNACIÓN DE MESAS.
 *
 * Muestra el botón '←' para volver, el título "Asignación de Mesas" y el subtítulo
 * con el rol y nombre del empleado (ej: "Camarera: Camila Gómez").
 */
@Composable
fun AsignacionHeader(
    nombreEmpleado: String,
    rolEmpleado: String,
    onVolverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón '←' para regresar a la pantalla anterior
        IconButton(onClick = onVolverClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Título y Subtítulo con el nombre del camarero/empleado
        Column {
            Text(
                text = stringResource(R.string.asignacion_titulo),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = stringResource(R.string.asignacion_subtitulo_formato, rolEmpleado, nombreEmpleado),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
