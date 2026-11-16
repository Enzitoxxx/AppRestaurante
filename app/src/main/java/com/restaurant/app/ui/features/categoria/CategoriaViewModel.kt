package com.restaurant.app.ui.features.categoria

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoriaViewModel : ViewModel() {

    // TODO: pasar a domain/data cuando exista repositorio de categorías
    private val _uiState = MutableStateFlow(
        CategoriaUiState(
            categorias = listOf(
                CategoriaUi("1", "Entradas", "Empanadas, rabas y picadas para compartir.", 1),
                CategoriaUi("2", "Platos principales", "Carnes a la parrilla, pastas caseras y minutas.", 1),
                CategoriaUi("3", "Bebidas", "Gaseosas, cervezas artesanales, vinos y tragos.", 1),
                CategoriaUi("4", "Postres", "Flan casero, helados y postres tradicionales.", 1),
                CategoriaUi("5", "Promociones", "Combos especiales y descuentos del día.", 0)
            )
        )
    )
    val uiState: StateFlow<CategoriaUiState> = _uiState.asStateFlow()

    fun onSolicitarEliminarCategoria(categoria: CategoriaUi) {
        _uiState.update { it.copy(categoriaADarDeBaja = categoria) }
    }

    fun onCancelarEliminarCategoria() {
        _uiState.update { it.copy(categoriaADarDeBaja = null) }
    }

    fun onConfirmarEliminarCategoria() {
        val categoria = _uiState.value.categoriaADarDeBaja ?: return
        _uiState.update { it.copy(categoriaADarDeBaja = null) }

        if (categoria.cantidadProductos > 0) {
            _uiState.update {
                it.copy(snackbarMensaje = "No se puede eliminar: tiene ${categoria.cantidadProductos} productos asociados")
            }
        } else {
            _uiState.update { state ->
                state.copy(categorias = state.categorias.filterNot { it.id == categoria.id })
            }
        }
    }

    fun onSnackbarMostrado() {
        _uiState.update { it.copy(snackbarMensaje = null) }
    }
}
