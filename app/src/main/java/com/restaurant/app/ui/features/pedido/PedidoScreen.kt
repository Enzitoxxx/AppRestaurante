package com.restaurant.app.ui.features.pedido

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppBottomBar
import com.restaurant.app.core.designsystem.AppTextField
import com.restaurant.app.core.designsystem.NavegacionItem
import com.restaurant.app.domain.model.Pedido
import com.restaurant.app.ui.features.pedido.components.PedidoCard
import com.restaurant.app.ui.features.pedido.components.PedidoFilterChips
import com.restaurant.app.ui.features.pedido.components.PedidoHeader
import com.restaurant.app.ui.theme.AppTheme

@Composable
fun PedidoScreen(
    onPedidoClick: (Pedido) -> Unit,
    onNavegarA: (NavegacionItem) -> Unit,
    viewModel: PedidoViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PedidoContent(
        state = state,
        onFiltroSelected = viewModel::onFiltroSelected,
        onBusquedaChange = viewModel::onBusquedaChange,
        onPedidoClick = onPedidoClick,
        onNavegarA = onNavegarA
    )
}

@Composable
fun PedidoContent(
    state: PedidoUiState,
    onFiltroSelected: (FiltroPedido) -> Unit,
    onBusquedaChange: (String) -> Unit,
    onPedidoClick: (Pedido) -> Unit,
    onNavegarA: (NavegacionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            PedidoHeader(cantidadActivas = state.cantidadAbiertos)

            Spacer(Modifier.height(16.dp))

            // Búsqueda por mesa o camarero (RF13)
            AppTextField(
                label = stringResource(R.string.pedidos_buscar),
                value = state.busqueda,
                onValueChange = onBusquedaChange,
                leadingIcon = Icons.Outlined.Search
            )

            Spacer(Modifier.height(12.dp))

            // Filtros por estado (RF13)
            PedidoFilterChips(
                filtros = FiltroPedido.entries,
                filtroSeleccionado = state.filtro,
                onFiltroSelected = onFiltroSelected
            )

            Spacer(Modifier.height(16.dp))

            if (state.pedidosFiltrados.isEmpty()) {
                Text(
                    text = stringResource(R.string.pedidos_vacio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items = state.pedidosFiltrados, key = { it.id }) { pedido ->
                        PedidoCard(
                            pedido = pedido,
                            nombreMesa = state.nombreMesa(pedido),
                            nombreCamarero = state.nombreCamarero(pedido),
                            onClick = { onPedidoClick(pedido) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PedidoScreenPreview() {
    AppTheme {
        PedidoContent(
            state = PedidoUiState(),
            onFiltroSelected = {},
            onBusquedaChange = {},
            onPedidoClick = {},
            onNavegarA = {}
        )
    }
}