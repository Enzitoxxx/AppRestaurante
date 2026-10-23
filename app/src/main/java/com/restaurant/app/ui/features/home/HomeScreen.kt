package com.restaurant.app.ui.features.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.restaurant.app.ui.features.home.components.HomeHeader
import com.restaurant.app.ui.features.home.components.SalesSummaryCard
import com.restaurant.app.ui.features.home.components.StatusMetricsRow
import com.restaurant.app.ui.features.home.components.TopSellingDishesCard
import com.restaurant.app.ui.theme.AppTheme


/**Esta funion se encarga de obtener los datos del ViewModel y
 pasarselos a la pantalla*/

@Composable
fun HomeScreen(
    /**Obtiene los datos del ViewModel
     * Observa los cambios
     * Le pasa los datos a HomeContent()*/
    onNavegarA: (NavegacionItem) -> Unit, //funcion para solicitar que se abra otra pantalla
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    /**Significa que observa los datos del HomeViewModel y los convierte
     en un estado que Compose pueda utilizar
     Ej: Obtiene --> nombre del rest, fecha, ventas, pedidos..
     Si el estado cambia, Compose puede actualizar la interfaz*/

    /**Construye visualmente la pantalla
     * Coloca los componentes
     * Recibe los datos que debe mostrar*/
    HomeContent(
        state = state,
        onNavegarA = onNavegarA
    )
}
// --------------

@Composable
fun HomeContent(
    state: HomeUiState,
    onNavegarA: (NavegacionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AppBottomBar(
                rutaActual = NavegacionItem.Inicio.ruta,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            HomeHeader(nombreRestaurante = state.nombreRestaurante)

            Spacer(Modifier.height(20.dp))

            SalesSummaryCard(
                fecha = state.fechaHoy,
                totalVentas = state.totalVentasHoy,
                comparativa = state.porcentajeComparativa,
                transacciones = state.totalTransacciones
            )

            Spacer(Modifier.height(16.dp))

            StatusMetricsRow(
                pedidosAbiertos = state.pedidosAbiertos,
                mesasOcupadas = state.mesasOcupadas,
                totalMesas = state.totalMesas,
                porcentajeOcupacion = state.porcentajeOcupacion
            )

            Spacer(Modifier.height(16.dp))

            TopSellingDishesCard(platos = state.topPlatos)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeContent(
            state = HomeUiState(fechaHoy = "23 oct. 2026"),
            onNavegarA = {}
        )
    }
}