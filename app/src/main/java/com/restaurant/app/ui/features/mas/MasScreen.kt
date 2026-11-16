package com.restaurant.app.ui.features.mas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppBottomBar
import com.restaurant.app.core.designsystem.NavegacionItem
import com.restaurant.app.ui.features.mas.components.MasOptionCard
import com.restaurant.app.ui.theme.AppTheme

@Composable
fun MasScreen(
    onPedidosClick: () -> Unit,
    onCategoriasClick: () -> Unit,
    onSesionCerrada: () -> Unit,
    onNavegarA: (NavegacionItem) -> Unit,
    viewModel: MasViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                MasEvent.SesionCerrada -> onSesionCerrada()
            }
        }
    }

    MasContent(
        state = state,
        onPedidosClick = onPedidosClick,
        onCategoriasClick = onCategoriasClick,
        onSolicitarCerrarSesion = viewModel::onSolicitarCerrarSesion,
        onCancelarCerrarSesion = viewModel::onCancelarCerrarSesion,
        onConfirmarCerrarSesion = viewModel::onConfirmarCerrarSesion,
        onNavegarA = onNavegarA
    )
}

@Composable
fun MasContent(
    state: MasUiState,
    onPedidosClick: () -> Unit,
    onCategoriasClick: () -> Unit,
    onSolicitarCerrarSesion: () -> Unit,
    onCancelarCerrarSesion: () -> Unit,
    onConfirmarCerrarSesion: () -> Unit,
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
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.mas_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(20.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MasOptionCard(
                    titulo = stringResource(R.string.mas_opcion_pedidos_titulo),
                    subtitulo = stringResource(R.string.mas_opcion_pedidos_subtitulo),
                    icono = Icons.AutoMirrored.Outlined.ReceiptLong,
                    onClick = onPedidosClick
                )

                MasOptionCard(
                    titulo = stringResource(R.string.mas_opcion_categorias_titulo),
                    subtitulo = stringResource(R.string.mas_opcion_categorias_subtitulo),
                    icono = Icons.Outlined.Category,
                    onClick = onCategoriasClick
                )

                MasOptionCard(
                    titulo = stringResource(R.string.mas_opcion_cerrar_sesion_titulo),
                    subtitulo = stringResource(R.string.mas_opcion_cerrar_sesion_subtitulo),
                    icono = Icons.AutoMirrored.Outlined.Logout,
                    onClick = onSolicitarCerrarSesion,
                    isDestructive = true
                )
            }
        }

        if (state.mostrarDialogoCerrarSesion) {
            AlertDialog(
                onDismissRequest = onCancelarCerrarSesion,
                title = {
                    Text(
                        text = stringResource(R.string.mas_dialog_cerrar_sesion_titulo),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.mas_dialog_cerrar_sesion_mensaje),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(onClick = onConfirmarCerrarSesion) {
                        Text(
                            text = stringResource(R.string.mas_dialog_cerrar_sesion_confirmar),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = onCancelarCerrarSesion) {
                        Text(text = stringResource(R.string.mas_dialog_cerrar_sesion_cancelar))
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MasScreenPreview() {
    AppTheme {
        MasContent(
            state = MasUiState(),
            onPedidosClick = {},
            onCategoriasClick = {},
            onSolicitarCerrarSesion = {},
            onCancelarCerrarSesion = {},
            onConfirmarCerrarSesion = {},
            onNavegarA = {}
        )
    }
}
