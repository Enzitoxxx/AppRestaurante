package com.restaurant.app.ui.features.comanda

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.core.designsystem.AppBottomBar
import com.restaurant.app.core.designsystem.NavegacionItem
import com.restaurant.app.ui.features.comanda.components.ComandaCard
import com.restaurant.app.ui.features.comanda.components.ComandaFilterChips
import com.restaurant.app.ui.features.comanda.components.ComandaHeader
import com.restaurant.app.ui.theme.AppTheme
import com.restaurant.app.domain.model.ComandaModel

@Composable
fun ComandaScreen(
    onComandaClick: (ComandaModel) -> Unit,
    onNavegarA: (NavegacionItem) -> Unit,
    viewModel: ComandaViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ComandaContent(
        state = state,
        onFiltroSelected = viewModel::onFiltroSelected,
        onComandaClick = onComandaClick, //se reenvia a content
        onNavegarA = onNavegarA
    )
}

@Composable
fun ComandaContent(
    state: ComandaUiState,
    onFiltroSelected: (String) -> Unit,
    onComandaClick: (ComandaModel) -> Unit,
    onNavegarA: (NavegacionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AppBottomBar(
                rutaActual = NavegacionItem.Mas.ruta, // Muestra el ítem "Más" seleccionado
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

            // Encabezado
            ComandaHeader(cantidadActivas = state.cantidadActivas)

            Spacer(Modifier.height(16.dp))

            // Chips de Filtro ("Todos", "En preparación", "Cobrados")
            ComandaFilterChips(
                filtros = state.filtrosDisponibles,
                filtroSeleccionado = state.filtroEstado,
                onFiltroSelected = onFiltroSelected
            )

            Spacer(Modifier.height(16.dp))

            // Lista de Comandas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = state.comandasFiltradas,
                    key = { it.id }
                ) { comanda ->
                    ComandaCard(
                        comanda = comanda,
                        onClick = { onComandaClick(comanda) } // ASIGNADO CLIC
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ComandaScreenPreview() {
    AppTheme {
        ComandaContent(
            state = ComandaUiState(),
            onFiltroSelected = {},
            onComandaClick = {},
            onNavegarA = {}
        )
    }
}