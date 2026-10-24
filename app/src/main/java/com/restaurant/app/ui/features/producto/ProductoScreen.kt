package com.restaurant.app.ui.features.producto

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.ui.features.producto.components.AreaPreparacionSelector
import com.restaurant.app.ui.features.producto.components.DisponibilidadCard
import com.restaurant.app.ui.features.producto.components.ProductoFormFields
import com.restaurant.app.ui.features.producto.components.ProductoHeader
import com.restaurant.app.ui.theme.AppTheme

/**
 * PANTALLA ALTA / EDICIÓN DE PRODUCTO (Stateful Composable).
 * Conectada a ProductoViewModel: gestiona el formulario y escucha eventos de guardado o retorno.
 */
@Composable
fun ProductoScreen(
    productoId: String? = null,
    onVolverClick: () -> Unit,
    onProductoGuardado: () -> Unit,
    viewModel: ProductoViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Si viene un productoId (modo edición), cargamos ese producto desde el repositorio
    LaunchedEffect(productoId) {
        if (productoId != null) {
            viewModel.cargarProducto(productoId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ProductoEvent.ProductoGuardado -> onProductoGuardado()
                ProductoEvent.Volver -> onVolverClick()
            }
        }
    }

    ProductoContent(
        state = state,
        onNombreChange = viewModel::onNombreChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onPrecioChange = viewModel::onPrecioChange,
        onCategoriaChange = viewModel::onCategoriaChange,
        onAreaChange = viewModel::onAreaChange,
        onToggleDisponible = viewModel::onToggleDisponible,
        onVolverClick = viewModel::onVolverClick,
        onGuardarClick = viewModel::onGuardarClick
    )
}

/**
 * CONTENIDO VISUAL DEL FORMULARIO DE PRODUCTO (Stateless Composable).
 * Sin ViewModel: fácil de probar e inspeccionar en la vista previa.
 */
@Composable
fun ProductoContent(
    state: ProductoUiState,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onAreaChange: (String) -> Unit,
    onToggleDisponible: (Boolean) -> Unit,
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
            // 1. Encabezado con botones '←', Título y botón 'Guardar'
            ProductoHeader(
                esEdicion = state.esEdicion,
                estaCargando = state.estaCargando,
                onVolverClick = onVolverClick,
                onGuardarClick = onGuardarClick
            )

            Spacer(Modifier.height(20.dp))

            // 2. Campos del Formulario (Nombre, Descripción, Precio, Categoría)
            ProductoFormFields(
                state = state,
                onNombreChange = onNombreChange,
                onDescripcionChange = onDescripcionChange,
                onPrecioChange = onPrecioChange,
                onCategoriaChange = onCategoriaChange
            )

            Spacer(Modifier.height(16.dp))

            // 3. Selector de Área de Preparación (Cocina / Barra)
            AreaPreparacionSelector(
                areaSeleccionada = state.areaPreparacion,
                onAreaChange = onAreaChange
            )

            Spacer(Modifier.height(16.dp))

            // 4. Tarjeta Switch de Disponibilidad
            DisponibilidadCard(
                disponible = state.disponibleParaVenta,
                onToggleDisponible = onToggleDisponible
            )
        }
    }
}

/** Previsualización en tiempo de diseño en Android Studio. */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProductoScreenPreview() {
    AppTheme {
        ProductoContent(
            state = ProductoUiState(
                nombre = "Empanadas de carne",
                descripcion = "Carne cortada a cuchillo con cebolla de verdeo, huevo y aceitunas. Masa criolla frita.",
                precio = "2200",
                categoria = "Entradas",
                areaPreparacion = "Cocina",
                disponibleParaVenta = true
            ),
            onNombreChange = {},
            onDescripcionChange = {},
            onPrecioChange = {},
            onCategoriaChange = {},
            onAreaChange = {},
            onToggleDisponible = {},
            onVolverClick = {},
            onGuardarClick = {}
        )
    }
}
