package com.restaurant.app.ui.features.carta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
//import com.restaurant.app.data.repository.ProductoRepositoryDatos
import com.restaurant.app.domain.model.Producto
import com.restaurant.app.domain.repository.ProductoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartaViewModel(
    //private val productoRepository: ProductoRepository = ProductoRepositoryDatos()
    private val productoRepository: ProductoRepository = Repositorios.productos
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartaUiState())
    val uiState: StateFlow<CartaUiState> = _uiState.asStateFlow()

    private val _events = Channel<CartaEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        // Escuchamos los productos del repositorio en tiempo real
        viewModelScope.launch {
            productoRepository.observarProductos().collect { lista ->
                _uiState.update { it.copy(productos = lista) }
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
        viewModelScope.launch {
            productoRepository.eliminarProducto(productoAEliminar.id)
            _uiState.update { it.copy(productoADarDeBaja = null) }
        }
    }
}