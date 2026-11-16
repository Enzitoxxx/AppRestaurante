package com.restaurant.app.ui.features.categoria.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import com.restaurant.app.ui.features.categoria.CategoriaUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriaFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaFormUiState())
    val uiState: StateFlow<CategoriaFormUiState> = _uiState.asStateFlow()

    private val _events = Channel<CategoriaFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // TODO: pasar a domain/data cuando exista repositorio de categorías
    private val categoriasEjemplo = listOf(
        CategoriaUi("1", "Entradas", "Empanadas, rabas y picadas para compartir.", 1),
        CategoriaUi("2", "Platos principales", "Carnes a la parrilla, pastas caseras y minutas.", 1),
        CategoriaUi("3", "Bebidas", "Gaseosas, cervezas artesanales, vinos y tragos.", 1),
        CategoriaUi("4", "Postres", "Flan casero, helados y postres tradicionales.", 1),
        CategoriaUi("5", "Promociones", "Combos especiales y descuentos del día.", 0)
    )

    fun cargarCategoria(id: String) {
        val cat = categoriasEjemplo.find { it.id == id } ?: return
        _uiState.update {
            it.copy(
                nombre = cat.nombre,
                descripcion = cat.descripcion,
                esEdicion = true
            )
        }
    }

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(nombre = value, nombreError = null) }
    }

    fun onDescripcionChange(value: String) {
        _uiState.update { it.copy(descripcion = value) }
    }

    fun onVolverClick() {
        viewModelScope.launch {
            _events.send(CategoriaFormEvent.Volver)
        }
    }

    fun onGuardarClick() {
        val state = _uiState.value
        if (state.estaCargando) return

        val nombre = state.nombre.trim()
        if (nombre.isBlank()) {
            _uiState.update { it.copy(nombreError = R.string.categoria_form_error_nombre_requerido) }
            return
        }

        // TODO: validar nombre repetido (categoria_form_error_nombre_repetido) cuando exista repositorio

        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            delay(1000)
            _uiState.update { it.copy(estaCargando = false) }
            _events.send(CategoriaFormEvent.CategoriaGuardada)
        }
    }
}
