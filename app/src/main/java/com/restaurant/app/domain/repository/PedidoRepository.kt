package com.restaurant.app.domain.repository

import com.restaurant.app.domain.model.Pedido
import kotlinx.coroutines.flow.Flow

interface PedidoRepository {

    fun observarPedidos(): Flow<List<Pedido>>

    suspend fun obtenerPedido(id: String): Pedido?

    // Pasa el pedido a COBRADO. Devuelve false si no existe o ya no está abierto.
    suspend fun cobrarPedido(id: String): Boolean

    // Pasa el pedido a ANULADO y registra motivo, fecha y usuario (RF15).
    // Devuelve false si no existe, no está abierto o no hay motivo.
    suspend fun anularPedido(id: String, motivo: String, usuario: String): Boolean
}