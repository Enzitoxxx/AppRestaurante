package com.restaurant.app.ui.features.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.model.Producto
import com.restaurant.app.domain.repository.ProductoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/*
    1. Recibir los cambios de cada campo mientras el usuario escribe.
    2. Permitir solo números en el campo Precio.
    3. En modo Edición, cargar el producto desde el repositorio.
    4. Validar nombre y precio al presionar "Guardar".
    5. Guardar en el repositorio (alta o edición) y avisar con ProductoEvent.
*/
class ProductoViewModel(
    private val repositorio: ProductoRepository = Repositorios.productos
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProductoEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // null = estamos creando un producto nuevo. Con valor = editando ese producto.
    private var productoId: String? = null

    // MODO EDICIÓN: busca el producto en el repositorio y precarga el formulario
    fun cargarProducto(id: String) {
        if (productoId == id) return   // ya se cargó: no pisar lo que el usuario esté editando
        viewModelScope.launch {
            val producto = repositorio.obtenerProducto(id) ?: return@launch
            productoId = producto.id
            _uiState.update {
                it.copy(
                    nombre = producto.nombre,
                    descripcion = producto.descripcion,
                    precio = producto.precio.toInt().toString(),
                    categoria = producto.categoria,
                    areaPreparacion = producto.area,
                    disponibleParaVenta = producto.estadoDisponible,
                    esEdicion = true
                )
            }
        }
    }

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, errorNombre = null) }
    }

    fun onDescripcionChange(value: String) {
        _uiState.update { it.copy(descripcion = value) }
    }

    fun onPrecioChange(value: String) {
        // Solo permite ingresar dígitos numéricos
        if (value.all { it.isDigit() }) {
            _uiState.update { it.copy(precio = value, errorPrecio = null) }
        }
    }

    fun onCategoriaChange(value: String) {
        _uiState.update { it.copy(categoria = value) }
    }

    fun onAreaChange(value: String) {
        _uiState.update { it.copy(areaPreparacion = value) }
    }

    fun onToggleDisponible(value: Boolean) {
        _uiState.update { it.copy(disponibleParaVenta = value) }
    }

    fun onVolverClick() {
        _events.trySend(ProductoEvent.Volver)
    }

    fun onGuardarClick() {
        val state = _uiState.value
        if (state.estaCargando) return

        val nombre = state.nombre.trim()
        val precio = state.precio.trim().toDoubleOrNull()

        // Validaciones (RF07: nombre obligatorio y precio mayor a cero)
        val errorNombre = if (nombre.isBlank()) R.string.producto_error_nombre_requerido else null
        val errorPrecio = when {
            state.precio.isBlank() -> R.string.producto_error_precio_requerido
            precio == null || precio <= 0 -> R.string.producto_error_precio_mayor_cero
            else -> null
        }

        if (errorNombre != null || errorPrecio != null) {
            _uiState.update { it.copy(errorNombre = errorNombre, errorPrecio = errorPrecio) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }

            val producto = Producto(
                // Si estamos editando, conserva el id. Si es nuevo, genera uno.
                id = productoId ?: UUID.randomUUID().toString(),
                nombre = nombre,
                descripcion = state.descripcion.trim(),
                categoria = state.categoria,
                area = state.areaPreparacion,
                precio = precio!!,
                estadoDisponible = state.disponibleParaVenta,
                estadoTexto = if (state.disponibleParaVenta) null else "Pausado"
            )
            repositorio.guardarProducto(producto)

            _uiState.update { it.copy(estaCargando = false) }
            _events.send(ProductoEvent.ProductoGuardado)
        }
    }
}