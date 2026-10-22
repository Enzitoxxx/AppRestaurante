package com.restaurant.app.ui.features.carta

import androidx.lifecycle.ViewModel
import com.restaurant.app.data.model.Producto
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class CartaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartaUiState())
    val uiState: StateFlow<CartaUiState> = _uiState.asStateFlow()

    private val _events = Channel<CartaEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        cargarProductosFicticios()
    }

    private fun cargarProductosFicticios() {
        val listaFicticia = listOf(
            Producto(
                id = "1",
                nombre = "Empanadas de carne",
                categoria = "Entradas",
                area = "Cocina",
                precio = 2200.0,
                estadoDisponible = true
            ),
            Producto(
                id = "2",
                nombre = "Bife de chorizo con papas",
                categoria = "Platos principales",
                area = "Cocina",
                precio = 14800.0,
                estadoDisponible = true
            ),
            Producto(
                id = "3",
                nombre = "Cerveza IPA Artesanal",
                categoria = "Bebidas",
                area = "Bar",
                precio = 3500.0,
                estadoDisponible = false,
                estadoTexto = "Pausado por stock"
            ),
            Producto(
                id = "4",
                nombre = "Flan casero mixto",
                categoria = "Postres",
                area = "Cocina",
                precio = 3200.0,
                estadoDisponible = true
            )
        )
        _uiState.update { it.copy(productos = listaFicticia) }
    }

    // Funciones de estado e interacción del usuario

    fun onBusquedaChange(nuevaBusqueda: String) {
        _uiState.update { it.copy(busqueda = nuevaBusqueda) }
    }

    fun onCategoriaChange(categoria: String) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria) }
    }

    fun onToggleDisponibilidadProducto(productoId: String) {
        _uiState.update { state ->
            val productosActualizados = state.productos.map { producto ->
                if (producto.id == productoId) {
                    producto.copy(estadoDisponible = !producto.estadoDisponible)
                } else {
                    producto
                }
            }
            state.copy(productos = productosActualizados)
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

        _uiState.update { state ->
            val listaSinProducto = state.productos.filterNot { it.id == productoAEliminar.id }
            state.copy(
                productos = listaSinProducto,
                productoADarDeBaja = null
            )
        }
    }
}
