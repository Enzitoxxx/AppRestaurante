package com.restaurant.app.ui.features.carta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.model.Producto
import com.restaurant.app.domain.repository.PedidoRepository
import com.restaurant.app.domain.repository.ProductoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
 * VIEWMODEL DE LA CARTA & MENÚ (CartaViewModel).
 *
 * DIFERENCIA ENTRE BAJA LÓGICA Y BAJA FÍSICA DE PRODUCTOS:
 * - Baja Lógica (estaDadoDeBaja = true): Ocurre cuando se intenta eliminar un producto que ya figura en pedidos
 *   registrados. No se elimina de la base de datos para no romper el historial de ventas del restaurante,
 *   sino que se marca como dado de baja para ocultarlo de la Carta.
 * - Baja Física (eliminarProducto): Se realiza cuando el producto no tiene pedidos asociados. Se borra su registro
 *   definitivamente de SQLite.
 */
class CartaViewModel(
    private val productoRepository: ProductoRepository = Repositorios.productos,
    private val pedidoRepository: PedidoRepository = Repositorios.pedidos
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartaUiState())
    val uiState: StateFlow<CartaUiState> = _uiState.asStateFlow()

    private val _events = Channel<CartaEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        // Escuchamos los productos y pedidos en tiempo real
        viewModelScope.launch {
            combine(
                productoRepository.observarProductos(),
                pedidoRepository.observarPedidos()
            ) { productos, pedidos ->
                productos to pedidos
            }.collect { (productos, pedidos) ->
                _uiState.update { it.copy(productos = productos, pedidos = pedidos) }
            }
        }
    }

    fun onBusquedaChange(nuevaBusqueda: String) {
        _uiState.update { it.copy(busqueda = nuevaBusqueda) }
    }

    fun onCategoriaChange(categoria: String) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria) }
    }

    fun onToggleDisponibilidadProducto(productoId: String) {
        viewModelScope.launch {
            val producto = productoRepository.obtenerProducto(productoId) ?: return@launch
            val nuevo = !producto.estadoDisponible
            val productoActualizado = producto.copy(
                estadoDisponible = nuevo,
                estadoTexto = if (nuevo) null else "Pausado"
            )
            productoRepository.guardarProducto(productoActualizado)
        }
    }

    fun onAgregarProductoClick() {
        _events.trySend(CartaEvent.NavegarAAgregarProducto)
    }

    fun onEditarProductoClick(producto: Producto) {
        _events.trySend(CartaEvent.NavegarAEditarProducto(producto))
    }

    fun onSolicitarBajaProducto(producto: Producto) {
        _uiState.update { it.copy(productoADarDeBaja = producto) }
    }

    fun onCancelarBajaProducto() {
        _uiState.update { it.copy(productoADarDeBaja = null) }
    }

    fun onConfirmarBajaProducto() {
        val productoAEliminar = _uiState.value.productoADarDeBaja ?: return
        _uiState.update { it.copy(productoADarDeBaja = null) }

        val figuraEnPedidos = _uiState.value.pedidos.any { pedido ->
            pedido.items.any { item -> item.productoId == productoAEliminar.id }
        }

        if (figuraEnPedidos) {
            viewModelScope.launch {
                val productoActualizado = productoAEliminar.copy(
                    estaDadoDeBaja = true
                )
                productoRepository.guardarProducto(productoActualizado)
                _events.send(CartaEvent.MostrarSnackbar("El producto figura en pedidos registrados: se dio de baja y se conserva en el historial"))
            }
        } else {
            viewModelScope.launch {
                productoRepository.eliminarProducto(productoAEliminar.id)
            }
        }
    }
}
