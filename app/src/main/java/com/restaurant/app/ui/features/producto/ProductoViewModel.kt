package com.restaurant.app.ui.features.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
    1.Recibir los cambios de cada campo mientras el usuario escribe.
    2.Permitir solo números en el campo Precio (value.all { it.isDigit() }).
    3.Cargar los datos existentes si se entra en modo Edición.
    4.Validar que Nombre y Precio no estén vacíos al presionar "Guardar".
    5.Emitir el evento ProductoEvent.ProductoGuardado o ProductoEvent.Volver.
*/
class ProductoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProductoEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // Si viene un producto existente, se precargan los datos para MODO EDICIÓN
    fun cargarProductoParaEdicion(
        nombre: String,
        descripcion: String,
        precio: Double,
        categoria: String,
        area: String,
        disponible: Boolean
    ) {
        _uiState.update {
            it.copy(
                nombre = nombre,
                descripcion = descripcion,
                precio = precio.toInt().toString(),
                categoria = categoria,
                areaPreparacion = area,
                disponibleParaVenta = disponible,
                esEdicion = true
            )
        }
    }

    // funciones para actualizar el estado según lo que escribe o elige el usuario:

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
        val precio = state.precio.trim()

        // Validaciones del formulario
        val errorNombre = if (nombre.isBlank()) R.string.producto_error_nombre_requerido else null
        val errorPrecio = if (precio.isBlank()) R.string.producto_error_precio_requerido else null

        if (errorNombre != null || errorPrecio != null) {
            _uiState.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorPrecio = errorPrecio
                )
            }
            return
        }

        // Simulación de guardado en segundo plano
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            delay(1000) // Simulación de llamada a BD o servidor
            _uiState.update { it.copy(estaCargando = false) }
            _events.send(ProductoEvent.ProductoGuardado)
        }
    }
}