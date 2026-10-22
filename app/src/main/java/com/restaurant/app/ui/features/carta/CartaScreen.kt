package com.restaurant.app.ui.features.carta

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.core.designsystem.AppBottomBar
import com.restaurant.app.core.designsystem.NavegacionItem
import com.restaurant.app.data.model.Producto
import com.restaurant.app.ui.features.carta.components.CartaCategoryFilter
import com.restaurant.app.ui.features.carta.components.CartaHeader
import com.restaurant.app.ui.features.carta.components.CartaItemCard
import com.restaurant.app.ui.features.carta.components.CartaSearchBar
import com.restaurant.app.ui.theme.AppTheme

/**
 * PANTALLA CARTA & MENÚ (Stateful Composable).
 * Conectada a CartaViewModel: observa el estado de productos y escucha eventos de navegación.
 */
@Composable
fun CartaScreen(
    onAgregarProductoClick: () -> Unit,
    onEditarProductoClick: (Producto) -> Unit = {},
    onNavegarA: (NavegacionItem) -> Unit = {},
    viewModel: CartaViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CartaEvent.NavegarAAgregarProducto -> onAgregarProductoClick()
                is CartaEvent.NavegarAEditarProducto -> onEditarProductoClick(event.producto)
            }
        }
    }

    CartaContent(
        state = state,
        onBusquedaChange = viewModel::onBusquedaChange,
        onCategoriaSeleccionada = viewModel::onCategoriaChange,
        onToggleDisponibilidadProducto = viewModel::onToggleDisponibilidadProducto,
        onAgregarProductoClick = viewModel::onAgregarProductoClick,
        onEditarProductoClick = viewModel::onEditarProductoClick,
        onSolicitarBajaProducto = viewModel::onSolicitarBajaProducto,
        onCancelarBajaProducto = viewModel::onCancelarBajaProducto,
        onConfirmarBajaProducto = viewModel::onConfirmarBajaProducto,
        onNavegarA = onNavegarA
    )
}

/**
 * CONTENIDO VISUAL DE LA CARTA (Stateless Composable).
 * Solo dibuja a partir de un estado. Sin ViewModel: fácil de previsualizar y probar.
 */
@Composable
fun CartaContent(
    state: CartaUiState,
    onBusquedaChange: (String) -> Unit,
    onCategoriaSeleccionada: (String) -> Unit,
    onToggleDisponibilidadProducto: (String) -> Unit,
    onAgregarProductoClick: () -> Unit,
    onEditarProductoClick: (Producto) -> Unit = {},
    onSolicitarBajaProducto: (Producto) -> Unit = {},
    onCancelarBajaProducto: () -> Unit = {},
    onConfirmarBajaProducto: () -> Unit = {},
    onNavegarA: (NavegacionItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            AppBottomBar(
                rutaActual = "carta",
                onItemClick = { item -> onNavegarA(item) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 1. Encabezado "Carta & Menú" + Botón Naranja '+'
            CartaHeader(onAgregarProductoClick = onAgregarProductoClick)

            Spacer(Modifier.height(16.dp))

            // 2. Barra de Búsqueda "Buscar plato o bebida..."
            CartaSearchBar(
                busqueda = state.busqueda,
                onBusquedaChange = onBusquedaChange
            )

            Spacer(Modifier.height(14.dp))

            // 3. Filtros Horizontales por Categoría
            CartaCategoryFilter(
                categoriaSeleccionada = state.categoriaSeleccionada,
                onCategoriaSeleccionada = onCategoriaSeleccionada,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // 4. Lista LazyColumn de Tarjetas de Productos
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(
                    items = state.productosFiltrados,
                    key = { producto -> producto.id }
                ) { producto ->
                    CartaItemCard(
                        producto = producto,
                        onToggleDisponibilidad = onToggleDisponibilidadProducto,
                        onEditarClick = onEditarProductoClick,
                        onBajaClick = onSolicitarBajaProducto
                    )
                }
            }
        }

        // Diálogo Emergente (AlertDialog) de Confirmación para Baja Lógica
        state.productoADarDeBaja?.let { producto ->
            AlertDialog(
                onDismissRequest = onCancelarBajaProducto,
                title = {
                    Text(
                        text = "¿Dar de baja producto?",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro de dar de baja \"${producto.nombre}\"? Dejará de mostrarse en la carta y comandas.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(onClick = onConfirmarBajaProducto) {
                        Text(
                            text = "Dar de baja",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = onCancelarBajaProducto) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }
    }
}

/** Previsualización en tiempo de diseño dentro de Android Studio. */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CartaScreenPreview() {
    AppTheme {
        CartaContent(
            state = CartaUiState(
                productos = listOf(
                    Producto("1", "Empanadas de carne", "Entradas", "Cocina", 2200.0, true),
                    Producto("2", "Bife de chorizo con papas", "Platos principales", "Cocina", 14800.0, true),
                    Producto("3", "Cerveza IPA Artesanal", "Bebidas", "Bar", 3500.0, false, "Pausado por stock"),
                    Producto("4", "Flan casero mixto", "Postres", "Cocina", 3200.0, true)
                )
            ),
            onBusquedaChange = {},
            onCategoriaSeleccionada = {},
            onToggleDisponibilidadProducto = {},
            onAgregarProductoClick = {}
        )
    }
}
