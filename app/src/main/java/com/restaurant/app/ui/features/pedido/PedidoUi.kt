package com.restaurant.app.ui.features.pedido

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.restaurant.app.R
import com.restaurant.app.domain.model.EstadoPedido
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EstiloEstadoPedido(
    val fondo: Color,
    val texto: Color,
    @StringRes val etiqueta: Int
)

fun EstadoPedido.estilo(): EstiloEstadoPedido = when (this) {
    EstadoPedido.EN_PREPARACION -> EstiloEstadoPedido(
        Color(0xFFFFF8E1), Color(0xFFD87D00), R.string.pedidos_estado_en_preparacion
    )
    EstadoPedido.LISTO_PARA_SERVIR -> EstiloEstadoPedido(
        Color(0xFFE8F5E9), Color(0xFF2E7D32), R.string.pedidos_estado_listo
    )
    EstadoPedido.ESPERANDO_CUENTA -> EstiloEstadoPedido(
        Color(0xFFF3E5F5), Color(0xFF7B1FA2), R.string.pedidos_estado_esperando_cuenta
    )
    EstadoPedido.COBRADO -> EstiloEstadoPedido(
        Color(0xFFE3F2FD), Color(0xFF1565C0), R.string.pedidos_estado_cobrado
    )
    EstadoPedido.ANULADO -> EstiloEstadoPedido(
        Color(0xFFFFEBEE), Color(0xFFC62828), R.string.pedidos_estado_anulado
    )
}

// 1730000000000 -> "28/10 10:25"
fun formatearFechaHora(millis: Long): String =
    SimpleDateFormat("dd/MM HH:mm", Locale("es", "AR")).format(Date(millis))