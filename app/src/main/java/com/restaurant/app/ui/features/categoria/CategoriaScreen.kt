package com.restaurant.app.ui.features.categoria

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppBottomBar
import com.restaurant.app.core.designsystem.NavegacionItem
import com.restaurant.app.ui.features.categoria.components.CategoriaCard
import com.restaurant.app.ui.features.categoria.components.CategoriaHeader
import com.restaurant.app.ui.theme.AppTheme

@Composable
fun CategoriaScreen(
    onNuevaCategoriaClick: () -> Unit,
    onEditarCategoriaClick: (String) -> Unit,
    onNavegarA: (NavegacionItem) -> Unit,
    viewModel: CategoriaViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    state.snackbarMensaje?.let { mensaje ->
        LaunchedEffect(mensaje) {
            snackbarHostState.showSnackbar(mensaje)
            viewModel.onSnackbarMostrado()
        }
    }

    CategoriaContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onAgregarCategoriaClick = onNuevaCategoriaClick,
        onEditarClick = { categoria -> onEditarCategoriaClick(categoria.id) },
        onEliminarClick = viewModel::onSolicitarEliminarCategoria,
        onCancelarEliminar = viewModel::onCancelarEliminarCategoria,
        onConfirmarEliminar = viewModel::onConfirmarEliminarCategoria,
        onNavegarA = onNavegarA
    )
}

@Composable
fun CategoriaContent(
    state: CategoriaUiState,
    snackbarHostState: SnackbarHostState,
    onAgregarCategoriaClick: () -> Unit,
    onEditarClick: (CategoriaUi) -> Unit,
    onEliminarClick: (CategoriaUi) -> Unit,
    onCancelarEliminar: () -> Unit,
    onConfirmarEliminar: () -> Unit,
    onNavegarA: (NavegacionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AppBottomBar(
                rutaActual = NavegacionItem.Mas.ruta,
                onItemClick = onNavegarA
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            CategoriaHeader(
                totalCategorias = state.totalCategorias,
                onAgregarCategoriaClick = onAgregarCategoriaClick
            )

            Spacer(Modifier.height(20.dp))

            if (state.categorias.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.categoria_vacio),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = state.categorias,
                        key = { it.id }
                    ) { categoria ->
                        CategoriaCard(
                            categoria = categoria,
                            onEditarClick = onEditarClick,
                            onEliminarClick = onEliminarClick
                        )
                    }
                }
            }
        }

        state.categoriaADarDeBaja?.let { categoria ->
            AlertDialog(
                onDismissRequest = onCancelarEliminar,
                title = {
                    Text(
                        text = stringResource(R.string.categoria_dialog_eliminar_titulo),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.categoria_dialog_eliminar_mensaje, categoria.nombre),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(onClick = onConfirmarEliminar) {
                        Text(
                            text = stringResource(R.string.categoria_dialog_eliminar_confirmar),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = onCancelarEliminar) {
                        Text(text = stringResource(R.string.categoria_dialog_eliminar_cancelar))
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CategoriaScreenPreview() {
    AppTheme {
        CategoriaContent(
            state = CategoriaUiState(
                categorias = listOf(
                    CategoriaUi("1", "Entradas", "Empanadas, rabas y picadas", 1),
                    CategoriaUi("2", "Promociones", "Combos especiales", 0)
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onAgregarCategoriaClick = {},
            onEditarClick = {},
            onEliminarClick = {},
            onCancelarEliminar = {},
            onConfirmarEliminar = {},
            onNavegarA = {}
        )
    }
}
