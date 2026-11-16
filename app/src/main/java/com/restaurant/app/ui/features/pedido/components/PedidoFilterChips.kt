package com.restaurant.app.ui.features.pedido.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.restaurant.app.ui.features.pedido.FiltroFecha
import com.restaurant.app.ui.features.pedido.FiltroPedido

@Composable
fun <T> FilterChipsRow(
    opciones: List<T>,
    opcionSeleccionada: T,
    onOpcionSelected: (T) -> Unit,
    getEtiquetaRes: (T) -> Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { opcion ->
            FilterChip(
                selected = opcion == opcionSeleccionada,
                onClick = { onOpcionSelected(opcion) },
                label = { Text(stringResource(getEtiquetaRes(opcion))) },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    labelColor = MaterialTheme.colorScheme.onSurface
                ),
                border = null
            )
        }
    }
}

@Composable
fun PedidoFilterChips(
    filtros: List<FiltroPedido>,
    filtroSeleccionado: FiltroPedido,
    onFiltroSelected: (FiltroPedido) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChipsRow(
        opciones = filtros,
        opcionSeleccionada = filtroSeleccionado,
        onOpcionSelected = onFiltroSelected,
        getEtiquetaRes = { it.etiqueta },
        modifier = modifier
    )
}

@Composable
fun PedidoFechaFilterChips(
    filtros: List<FiltroFecha>,
    filtroSeleccionado: FiltroFecha,
    onFiltroSelected: (FiltroFecha) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChipsRow(
        opciones = filtros,
        opcionSeleccionada = filtroSeleccionado,
        onOpcionSelected = onFiltroSelected,
        getEtiquetaRes = { it.etiqueta },
        modifier = modifier
    )
}