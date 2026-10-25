package com.restaurant.app.ui.features.comanda.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.ui.features.comanda.detalle.components.AnularPedidoBottomSheet
import com.restaurant.app.ui.features.comanda.detalle.components.ComandaItemsCard
import com.restaurant.app.ui.features.comanda.detalle.components.ComandaTotalesCard
import com.restaurant.app.ui.theme.AppTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetalleComandaScreen(
    comandaId: String,
    onVolverClick: () -> Unit,
    onComandaFinalizada: () -> Unit,
    viewModel: DetalleComandaViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                DetalleComandaEvent.ComandaCobrada -> onComandaFinalizada()
                DetalleComandaEvent.ComandaAnulada -> onComandaFinalizada()
            }
        }
    }

    DetalleComandaContent(
        state = state,
        onVolverClick = onVolverClick,
        onCobrarClick = viewModel::onCobrarClick,
        onAbrirAnularSheet = viewModel::onAbrirAnularSheet,
        onCerrarAnularSheet = viewModel::onCerrarAnularSheet,
        onMotivoSelected = viewModel::onMotivoSeleccionado,
        onConfirmarAnulacion = viewModel::onConfirmarAnulacion
    )
}

@Composable
fun DetalleComandaContent(
    state: DetalleComandaUiState,
    onVolverClick: () -> Unit,
    onCobrarClick: () -> Unit,
    onAbrirAnularSheet: () -> Unit,
    onCerrarAnularSheet: () -> Unit,
    onMotivoSelected: (String) -> Unit,
    onConfirmarAnulacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "AR")).apply {
        maximumFractionDigits = 0
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Cabecera superior con botón Atrás + Título + Subtítulo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onVolverClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = stringResource(R.string.detalle_comanda_header_title, state.comandaId),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(
                        R.string.detalle_comanda_header_subtitle,
                        state.numeroMesa,
                        state.camareroNombre,
                        state.cantidadComensales
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Lista de ítems pedidos
        ComandaItemsCard(items = state.items)

        Spacer(Modifier.height(16.dp))

        // Tarjeta de Subtotal y Total
        ComandaTotalesCard(subtotal = state.subtotal, total = state.total)

        Spacer(Modifier.height(32.dp))

        // Botón Principal: Cobrar Comanda
        Button(
            onClick = onCobrarClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Payments,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.padding(start = 8.dp))
            Text(
                text = stringResource(R.string.detalle_comanda_btn_cobrar, currencyFormatter.format(state.total)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(12.dp))

        // Botón Secundario: Anular Pedido
        OutlinedButton(
            onClick = onAbrirAnularSheet,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
        ) {
            Icon(
                imageVector = Icons.Outlined.Cancel,
                contentDescription = null,
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.padding(start = 8.dp))
            Text(
                text = stringResource(R.string.detalle_comanda_btn_anular),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F)
            )
        }
    }

    // Modal para Anular Pedido (RF15)
    if (state.mostrarAnularSheet) {
        AnularPedidoBottomSheet(
            comandaId = state.comandaId,
            numeroMesa = state.numeroMesa,
            motivos = state.motivosAnulacion,
            motivoSeleccionado = state.motivoAnulacionSeleccionado,
            onMotivoSelected = onMotivoSelected,
            onConfirmarAnulacion = onConfirmarAnulacion,
            onDismiss = onCerrarAnularSheet
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetalleComandaScreenPreview() {
    AppTheme {
        DetalleComandaContent(
            state = DetalleComandaUiState(),
            onVolverClick = {},
            onCobrarClick = {},
            onAbrirAnularSheet = {},
            onCerrarAnularSheet = {},
            onMotivoSelected = {},
            onConfirmarAnulacion = {}
        )
    }
}