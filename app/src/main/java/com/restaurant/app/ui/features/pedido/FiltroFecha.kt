package com.restaurant.app.ui.features.pedido

import androidx.annotation.StringRes
import com.restaurant.app.R
import com.restaurant.app.domain.model.Pedido
import java.util.Calendar

enum class FiltroFecha(@StringRes val etiqueta: Int) {
    HOY(R.string.pedidos_filtro_fecha_hoy),
    ULTIMOS_7_DIAS(R.string.pedidos_filtro_fecha_ultimos_7_dias),
    TODAS(R.string.pedidos_filtro_fecha_todas);

    // true si el pedido entra en este rango de fecha respecto de la hora actual
    fun acepta(pedido: Pedido, ahora: Long = System.currentTimeMillis()): Boolean = when (this) {
        TODAS -> true
        HOY -> {
            val inicioHoy = Calendar.getInstance().apply {
                timeInMillis = ahora
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            pedido.fecha >= inicioHoy
        }
        ULTIMOS_7_DIAS -> {
            val inicioHace7Dias = Calendar.getInstance().apply {
                timeInMillis = ahora
                add(Calendar.DAY_OF_YEAR, -6)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            pedido.fecha >= inicioHace7Dias
        }
    }
}
