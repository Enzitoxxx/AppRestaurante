package com.restaurant.app.ui.features.carta.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.restaurant.app.R

@Composable
fun CartaCategoryFilter(
    categoriaSeleccionada: String,
    onCategoriaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categorias = listOf(
        stringResource(R.string.carta_categoria_todas),
        stringResource(R.string.carta_categoria_entradas),
        stringResource(R.string.carta_categoria_platos),
        stringResource(R.string.carta_categoria_bebidas),
        stringResource(R.string.carta_categoria_postres)
    )

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(categorias) { categoria ->
            val estaSeleccionado = categoriaSeleccionada.equals(categoria, ignoreCase = true)

            FilterChip(
                selected = estaSeleccionado,
                onClick = { onCategoriaSeleccionada(categoria) },
                label = { Text(text = categoria) },
                shape = RoundedCornerShape(18.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = null
            )
        }
    }
}
