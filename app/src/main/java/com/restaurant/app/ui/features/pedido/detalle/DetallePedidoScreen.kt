package com.restaurant.app.ui.features.pedido.detalle

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
import com.restaurant.app.domain.model.EstadoPedido
import com.restaurant.app.domain.model.ItemPedido
import com.restaurant.app.domain.model.Pedido
import com.restaurant.app.ui.features.pedido.detalle.components.AnularPedidoBottomSheet
import com.restaurant.app.ui.features.pedido.detalle.components.PedidoItemsCard
import com.restaurant.app.ui.features.pedido.detalle.components.PedidoTotalesCard
import com.restaurant.app.ui.features.pedido.estilo
import com.restaurant.app.ui.features.pedido.formatearFechaHora
import com.restaurant.app.ui.theme.AppTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetallePedidoScreen(
    pedidoId: String,
    onVolverClick: () -> Unit,
    onPedidoFinalizado: () -> Unit,
    viewModel: DetallePedidoViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Carga el pedido que se tocó en la lista
    LaunchedEffect(pedidoId) {
        viewModel.cargarPedido(pedidoId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                DetallePedidoEvent.PedidoCobrado -> onPedidoFinalizado()
                DetallePedidoEvent.PedidoAnulado -> onPedidoFinalizado()
            }
        }
    }

    DetallePedidoContent(
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
fun DetallePedidoContent(
    state: DetallePedidoUiState,
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
    val pedido = state.pedido

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Cabecera: botón atrás + número de pedido + mesa, camarero y comensales
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
                    text = stringResource(R.string.detalle_comanda_header_title, pedido?.id.orEmpty()),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (pedido != null) {
                    Text(
                        text = stringResource(
                            R.string.detalle_comanda_header_subtitle,
                            state.nombreMesa,
                            state.nombreCamarero,
                            pedido.comensales
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // Hora y estado del pedido (RF14)
                    val estilo = pedido.estado.estilo()
                    Text(
                        text = stringResource(
                            R.string.detalle_pedido_fecha_estado,
                            formatearFechaHora(pedido.fecha),
                            stringResource(estilo.etiqueta)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = estilo.texto
                    )
                }
            }
        }

        if (pedido != null) {
            Spacer(Modifier.height(24.dp))

            PedidoItemsCard(items = pedido.items)

            Spacer(Modifier.height(16.dp))

            PedidoTotalesCard(subtotal = pedido.total, total = pedido.total)

            Spacer(Modifier.height(32.dp))

            if (pedido.estado.estaAbierto) {
                // Solo los pedidos abiertos se pueden cobrar o anular (RF15)
                Button(
                    onClick = onCobrarClick,
                    enabled = !state.estaProcesando,
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
                        text = stringResource(
                            R.string.detalle_comanda_btn_cobrar,
                            currencyFormatter.format(pedido.total)
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onAbrirAnularSheet,
                    enabled = !state.estaProcesando,
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
            } else {
                // Pedido cerrado: si fue anulado, se muestra el motivo registrado
                pedido.motivoAnulacion?.let { motivo ->
                    Text(
                        text = stringResource(R.string.detalle_pedido_motivo, motivo),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // Hoja para anular el pedido (RF15)
    if (state.mostrarAnularSheet && pedido != null) {
        AnularPedidoBottomSheet(
            pedidoId = pedido.id,
            nombreMesa = state.nombreMesa,
            motivos = state.motivosAnulacion,
            motivoSeleccionado = state.motivoSeleccionado,
            onMotivoSelected = onMotivoSelected,
            onConfirmarAnulacion = onConfirmarAnulacion,
            onDismiss = onCerrarAnularSheet
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetallePedidoScreenPreview() {
    AppTheme {
        DetallePedidoContent(
            state = DetallePedidoUiState(
                pedido = Pedido(
                    id = "124", mesaId = "3", empleadoId = "2", comensales = 4,
                    fecha = System.currentTimeMillis(), estado = EstadoPedido.EN_PREPARACION,
                    items = listOf(
                        ItemPedido("1", "1", "Empanadas de carne", 2, 2200.0, "Con limón extra"),
                        ItemPedido("2", "2", "Bife de chorizo con papas", 1, 14800.0, "Punto medio")
                    )
                ),
                nombreMesa = "Mesa 03",
                nombreCamarero = "Camila Gómez"
            ),
            onVolverClick = {},
            onCobrarClick = {},
            onAbrirAnularSheet = {},
            onCerrarAnularSheet = {},
            onMotivoSelected = {},
            onConfirmarAnulacion = {}
        )
    }
}