package com.restaurant.app.ui.features.salon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppBottomBar
import com.restaurant.app.core.designsystem.NavegacionItem
import com.restaurant.app.domain.model.EstadoMesa
import com.restaurant.app.domain.model.Mesa
import com.restaurant.app.ui.features.salon.components.MesaCard
import com.restaurant.app.ui.features.salon.components.SalonHeader
import com.restaurant.app.ui.features.salon.components.ZonaHeader
import com.restaurant.app.ui.theme.AppTheme

/**
 * PANTALLA MAPA DEL SALÓN
 * Conectada al SalonViewModel: observa el estado de las mesas y escucha eventos de apertura, edición y baja.
 */
@Composable
fun SalonScreen(
    onMesaClick: (Mesa) -> Unit = {},
    onEditarMesaClick: (Mesa) -> Unit = {},
    onNuevaMesaClick: () -> Unit = {},
    onNavegarA: (NavegacionItem) -> Unit = {},
    viewModel: SalonViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SalonEvent.AbrirMesa -> onMesaClick(event.mesa)
                is SalonEvent.EditarMesa -> onEditarMesaClick(event.mesa)
                SalonEvent.NuevaMesa -> onNuevaMesaClick()
            }
        }
    }

    SalonContent(
        state = state,
        onZonaSeleccionada = viewModel::onZonaSeleccionada,
        onMesaClick = viewModel::onMesaClick,
        onEditarMesaClick = viewModel::onEditarMesaClick,
        onSolicitarBajaMesa = viewModel::onSolicitarBajaMesa,
        onCancelarBajaMesa = viewModel::onCancelarBajaMesa,
        onConfirmarBajaMesa = viewModel::onConfirmarBajaMesa,
        onNuevaMesaClick = viewModel::onNuevaMesaClick,
        onNavegarA = onNavegarA
    )
}

/**
 * CONTENIDO VISUAL DEL MAPA DEL SALÓN (Stateless Composable).
 * Sin ViewModel: fácil de probar y previsualizar en tiempo de diseño.
 */
@Composable
fun SalonContent(
    state: SalonUiState,
    onZonaSeleccionada: (String) -> Unit,
    onMesaClick: (Mesa) -> Unit,
    onEditarMesaClick: (Mesa) -> Unit = {},
    onSolicitarBajaMesa: (Mesa) -> Unit = {},
    onCancelarBajaMesa: () -> Unit = {},
    onConfirmarBajaMesa: () -> Unit = {},
    onNuevaMesaClick: () -> Unit = {},
    onNavegarA: (NavegacionItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            AppBottomBar(
                rutaActual = "salon",
                onItemClick = { item -> onNavegarA(item) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevaMesaClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar mesa",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // 1. Encabezado con Totales y Filtro Salón / Patio
                item {
                    SalonHeader(
                        ocupadasCount = state.ocupadasCount,
                        libresCount = state.libresCount,
                        cuentaCount = state.cuentaCount,
                        zonaSeleccionada = state.zonaSeleccionada,
                        onZonaSeleccionada = onZonaSeleccionada
                    )
                    Spacer(Modifier.height(20.dp))
                }

                // 2. Mesas de ZONA PRINCIPAL (SALÓN)
                val mesasSalon = state.mesasFiltradas.filter { it.zona.equals("Salón", ignoreCase = true) }
                if (mesasSalon.isNotEmpty()) {
                    item {
                        ZonaHeader(tituloZona = stringResource(R.string.salon_zona_principal))
                        Spacer(Modifier.height(12.dp))
                    }

                    // Cuadrícula de 2 columnas para Salón
                    val paresSalon = mesasSalon.chunked(2)
                    items(paresSalon.size) { index ->
                        val par = paresSalon[index]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MesaCard(
                                mesa = par[0],
                                onMesaClick = onMesaClick,
                                onEditarClick = onEditarMesaClick,
                                onBajaClick = onSolicitarBajaMesa,
                                modifier = Modifier.weight(1f)
                            )
                            if (par.size > 1) {
                                MesaCard(
                                    mesa = par[1],
                                    onMesaClick = onMesaClick,
                                    onEditarClick = onEditarMesaClick,
                                    onBajaClick = onSolicitarBajaMesa,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }

                // 3. Mesas de ZONA EXTERIOR (PATIO)
                val mesasPatio = state.mesasFiltradas.filter { it.zona.equals("Patio", ignoreCase = true) }
                if (mesasPatio.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        ZonaHeader(tituloZona = stringResource(R.string.salon_zona_exterior))
                        Spacer(Modifier.height(12.dp))
                    }

                    // Cuadrícula de 2 columnas para Patio
                    val paresPatio = mesasPatio.chunked(2)
                    items(paresPatio.size) { index ->
                        val par = paresPatio[index]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MesaCard(
                                mesa = par[0],
                                onMesaClick = onMesaClick,
                                onEditarClick = onEditarMesaClick,
                                onBajaClick = onSolicitarBajaMesa,
                                modifier = Modifier.weight(1f)
                            )
                            if (par.size > 1) {
                                MesaCard(
                                    mesa = par[1],
                                    onMesaClick = onMesaClick,
                                    onEditarClick = onEditarMesaClick,
                                    onBajaClick = onSolicitarBajaMesa,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            // Diálogo Emergente (AlertDialog) de Confirmación para Baja Lógica de la Mesa
            state.mesaADarDeBaja?.let { mesa ->
                AlertDialog(
                    onDismissRequest = onCancelarBajaMesa,
                    title = {
                        Text(
                            text = "¿Dar de baja mesa?",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    text = {
                        Text(
                            text = "¿Estás seguro de dar de baja la ${mesa.numero}? Dejará de mostrarse en el mapa del salón.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = onConfirmarBajaMesa) {
                            Text(
                                text = "Dar de baja",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onCancelarBajaMesa) {
                            Text(text = "Cancelar")
                        }
                    }
                )
            }
        }
    }
}

/** Previsualización en tiempo de diseño en Android Studio. */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SalonScreenPreview() {
    AppTheme {
        SalonContent(
            state = SalonUiState(
                mesas = listOf(
                    Mesa("1", "Mesa 01", "Salón", 4, EstadoMesa.LIBRE, detalleEstado = "Sin comanda"),
                    Mesa("2", "Mesa 02", "Salón", 2, EstadoMesa.OCUPADA, mozoAsignado = "Camila G.", consumoTotal = 18200.0),
                    Mesa("3", "Mesa 03", "Salón", 4, EstadoMesa.OCUPADA, mozoAsignado = "Camila G.", consumoTotal = 28600.0),
                    Mesa("4", "Mesa 04", "Salón", 4, EstadoMesa.CUENTA, detalleEstado = "Esperando ticket", consumoTotal = 34100.0),
                    Mesa("5", "Mesa 05", "Patio", 2, EstadoMesa.LIBRE),
                    Mesa("6", "Mesa 06", "Patio", 6, EstadoMesa.OCUPADA, consumoTotal = 42000.0)
                )
            ),
            onZonaSeleccionada = {},
            onMesaClick = {},
            onNuevaMesaClick = {}
        )
    }
}
