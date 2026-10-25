package com.restaurant.app.ui.features.pedido

import androidx.annotation.StringRes
import com.restaurant.app.R
import com.restaurant.app.domain.model.EstadoPedido
import com.restaurant.app.domain.model.Pedido

enum class FiltroPedido(@StringRes val etiqueta: Int) {
    TODOS(R.string.pedidos_filtro_todos),
    ABIERTOS(R.string.pedidos_filtro_abiertos),
    EN_PREPARACION(R.string.pedidos_filtro_en_preparacion),
    LISTO_PARA_SERVIR(R.string.pedidos_filtro_listo),
    ESPERANDO_CUENTA(R.string.pedidos_filtro_esperando_cuenta),
    COBRADOS(R.string.pedidos_filtro_cobrados),
    ANULADOS(R.string.pedidos_filtro_anulados);

    // true si el pedido entra en este filtro
    fun acepta(pedido: Pedido): Boolean = when (this) {
        TODOS -> true
        ABIERTOS -> pedido.estado.estaAbierto
        EN_PREPARACION -> pedido.estado == EstadoPedido.EN_PREPARACION
        LISTO_PARA_SERVIR -> pedido.estado == EstadoPedido.LISTO_PARA_SERVIR
        ESPERANDO_CUENTA -> pedido.estado == EstadoPedido.ESPERANDO_CUENTA
        COBRADOS -> pedido.estado == EstadoPedido.COBRADO
        ANULADOS -> pedido.estado == EstadoPedido.ANULADO
    }
}