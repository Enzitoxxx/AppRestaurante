package com.restaurant.app.ui.features.pedido.detalle.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.restaurant.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnularPedidoBottomSheet(
    pedidoId: String,                      // antes: comandaId
    nombreMesa: String,                    // antes: numeroMesa
    motivos: List<String>,
    motivoSeleccionado: String?,           // antes: String (ahora puede no haber motivo)
    onMotivoSelected: (String) -> Unit,
    onConfirmarAnulacion: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Ícono de alerta en color rojo suave
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFEBEE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Título
            Text(
                text = stringResource(R.string.anular_pedido_titulo, pedidoId),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(4.dp))

            // Subtítulo
            Text(
                text = stringResource(R.string.anular_pedido_subtitulo, nombreMesa),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            // Opciones con RadioButton
            motivos.forEach { motivo ->
                val isSelected = motivo == motivoSeleccionado
                val borderColor = if (isSelected) Color(0xFFFFCDD2) else MaterialTheme.colorScheme.outlineVariant

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .border(1.dp, borderColor, MaterialTheme.shapes.medium)
                        .clickable { onMotivoSelected(motivo) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onMotivoSelected(motivo) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFD32F2F)
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = motivo,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(16.dp))

            // Botón Confirmar Anulación
            Button(
                onClick = onConfirmarAnulacion,
                enabled = motivoSeleccionado != null,                    // ← NUEVO
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFE53935).copy(alpha = 0.35f),   // ← NUEVO
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.anular_pedido_btn_confirmar),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            // Botón Volver Atrás
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = stringResource(R.string.anular_pedido_btn_volver),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}