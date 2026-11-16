package com.restaurant.app.ui.features.categoria.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppTextField
import com.restaurant.app.ui.features.categoria.form.components.CategoriaFormHeader
import com.restaurant.app.ui.theme.AppTheme

@Composable
fun CategoriaFormScreen(
    categoriaId: String? = null,
    onVolverClick: () -> Unit,
    onCategoriaGuardada: () -> Unit,
    viewModel: CategoriaFormViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(categoriaId) {
        if (categoriaId != null) {
            viewModel.cargarCategoria(categoriaId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CategoriaFormEvent.CategoriaGuardada -> onCategoriaGuardada()
                CategoriaFormEvent.Volver -> onVolverClick()
            }
        }
    }

    CategoriaFormContent(
        state = state,
        onNombreChange = viewModel::onNombreChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onVolverClick = viewModel::onVolverClick,
        onGuardarClick = viewModel::onGuardarClick
    )
}

@Composable
fun CategoriaFormContent(
    state: CategoriaFormUiState,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onVolverClick: () -> Unit,
    onGuardarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = maxHeight)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            CategoriaFormHeader(
                esEdicion = state.esEdicion,
                estaCargando = state.estaCargando,
                onVolverClick = onVolverClick,
                onGuardarClick = onGuardarClick
            )

            Spacer(Modifier.height(20.dp))

            AppTextField(
                label = stringResource(R.string.categoria_form_label_nombre),
                value = state.nombre,
                onValueChange = onNombreChange,
                leadingIcon = Icons.Outlined.Category,
                errorText = state.nombreError?.let { stringResource(it) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(Modifier.height(16.dp))

            AppTextField(
                label = stringResource(R.string.categoria_form_label_descripcion),
                value = state.descripcion,
                onValueChange = onDescripcionChange,
                leadingIcon = Icons.Outlined.Description,
                singleLine = false,
                minLines = 3,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onGuardarClick() })
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoriaFormScreenPreview() {
    AppTheme {
        CategoriaFormContent(
            state = CategoriaFormUiState(
                nombre = "Entradas",
                descripcion = "Empanadas, rabas y picadas",
                esEdicion = true
            ),
            onNombreChange = {},
            onDescripcionChange = {},
            onVolverClick = {},
            onGuardarClick = {}
        )
    }
}
