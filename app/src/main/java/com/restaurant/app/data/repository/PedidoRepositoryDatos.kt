package com.restaurant.app.data.repository

import com.restaurant.app.domain.model.EstadoPedido
import com.restaurant.app.domain.model.ItemPedido
import com.restaurant.app.domain.model.Pedido
import com.restaurant.app.domain.repository.PedidoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PedidoRepositoryDatos : PedidoRepository {

    // Las fechas de ejemplo se calculan respecto de "ahora", así siempre se ven recientes
    private val ahora = System.currentTimeMillis()
    private fun hace(minutos: Int): Long = ahora - minutos * 60_000L

    // Camila (id "2") es la única camarera de los datos de ejemplo
    private val pedidos = MutableStateFlow(
        listOf(
            Pedido(
                id = "124", mesaId = "3", empleadoId = "2", comensales = 4,
                fecha = hace(35), estado = EstadoPedido.EN_PREPARACION,
                items = listOf(
                    ItemPedido("1", "1", "Empanadas de carne", 2, 2200.0, "Con limón extra"),
                    ItemPedido("2", "2", "Bife de chorizo con papas", 1, 14800.0, "Punto medio"),
                    ItemPedido("3", "3", "Cerveza IPA Artesanal", 2, 3500.0),
                    ItemPedido("4", "4", "Flan casero mixto", 1, 2400.0)
                )
            ),
            Pedido(
                id = "123", mesaId = "2", empleadoId = "2", comensales = 2,
                fecha = hace(50), estado = EstadoPedido.LISTO_PARA_SERVIR,
                items = listOf(
                    ItemPedido("1", "2", "Bife de chorizo con papas", 1, 14800.0, "Bien cocido"),
                    ItemPedido("2", "3", "Cerveza IPA Artesanal", 1, 3500.0)
                )
            ),
            Pedido(
                id = "125", mesaId = "4", empleadoId = "2", comensales = 3,
                fecha = hace(80), estado = EstadoPedido.ESPERANDO_CUENTA,
                items = listOf(
                    ItemPedido("1", "2", "Bife de chorizo con papas", 2, 14800.0),
                    ItemPedido("2", "3", "Cerveza IPA Artesanal", 1, 3500.0)
                )
            ),
            Pedido(
                id = "122", mesaId = "6", empleadoId = "2", comensales = 6,
                fecha = hace(180), estado = EstadoPedido.COBRADO,
                items = listOf(
                    ItemPedido("1", "2", "Bife de chorizo con papas", 2, 14800.0),
                    ItemPedido("2", "3", "Cerveza IPA Artesanal", 2, 3500.0),
                    ItemPedido("3", "4", "Flan casero mixto", 2, 3200.0)
                )
            ),
            Pedido(
                id = "121", mesaId = "1", empleadoId = "2", comensales = 2,
                fecha = hace(300), estado = EstadoPedido.ANULADO,
                motivoAnulacion = "Cancelado por el cliente",
                fechaAnulacion = hace(290),
                anuladoPor = "Administrador",
                items = listOf(
                    ItemPedido("1", "1", "Empanadas de carne", 2, 2200.0)
                )
            ),
            Pedido(
                id = "120", mesaId = "5", empleadoId = "2", comensales = 2,
                fecha = hace(1500), estado = EstadoPedido.COBRADO,
                items = listOf(
                    ItemPedido("1", "1", "Empanadas de carne", 1, 2200.0),
                    ItemPedido("2", "4", "Flan casero mixto", 2, 3200.0)
                )
            )
        )
    )

    override fun observarPedidos(): Flow<List<Pedido>> = pedidos.asStateFlow()

    override suspend fun obtenerPedido(id: String): Pedido? =
        pedidos.value.find { it.id == id }

    override suspend fun cobrarPedido(id: String): Boolean =
        cambiarSiEstaAbierto(id) { it.copy(estado = EstadoPedido.COBRADO) }

    override suspend fun anularPedido(id: String, motivo: String, usuario: String): Boolean {
        if (motivo.isBlank()) return false      // RF15: el motivo es obligatorio
        return cambiarSiEstaAbierto(id) {
            it.copy(
                estado = EstadoPedido.ANULADO,
                motivoAnulacion = motivo,
                fechaAnulacion = System.currentTimeMillis(),
                anuladoPor = usuario
            )
        }
    }

    // Aplica el cambio solo si el pedido existe y sigue abierto
    private fun cambiarSiEstaAbierto(id: String, cambio: (Pedido) -> Pedido): Boolean {
        val pedido = pedidos.value.find { it.id == id } ?: return false
        if (!pedido.estado.estaAbierto) return false
        pedidos.update { lista -> lista.map { if (it.id == id) cambio(it) else it } }
        return true
    }
}