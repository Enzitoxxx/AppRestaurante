package com.restaurant.app.ui.features.producto.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppTextField
import com.restaurant.app.ui.features.producto.ProductoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormFields(
    state: ProductoUiState,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandidoCategoria by remember { mutableStateOf(false) }
    val categorias = listOf("Entradas", "Platos principales", "Bebidas", "Postres")
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. nombre del item (Reutilizando AppTextField)
        AppTextField(
            label = stringResource(R.string.producto_label_nombre),
            value = state.nombre,
            onValueChange = onNombreChange,
            errorText = state.errorNombre?.let { stringResource(it) },
            enabled = !state.estaCargando,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        // 2. descripcion (Reutilizando AppTextField)
        AppTextField(
            label = stringResource(R.string.producto_label_descripcion),
            value = state.descripcion,
            onValueChange = onDescripcionChange,
            singleLine = false,
            minLines = 3,
            enabled = !state.estaCargando,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        // 3. precio (Reutilizando AppTextField)
        AppTextField(
            label = stringResource(R.string.producto_label_precio),
            value = state.precio,
            onValueChange = onPrecioChange,
            errorText = state.errorPrecio?.let { stringResource(it) },
            enabled = !state.estaCargando,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        // 4. categoria
        ExposedDropdownMenuBox(
            expanded = expandidoCategoria,
            onExpandedChange = { expandidoCategoria = !expandidoCategoria },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.categoria,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.producto_label_categoria)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCategoria) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    focusedLabelColor = colors.primary
                )
            )

            ExposedDropdownMenu(
                expanded = expandidoCategoria,
                onDismissRequest = { expandidoCategoria = false }
            ) {
                categorias.forEach { opcionCategoria ->
                    DropdownMenuItem(
                        text = { Text(opcionCategoria) },
                        onClick = {
                            onCategoriaChange(opcionCategoria)
                            expandidoCategoria = false
                        }
                    )
                }
            }
        }
    }
}