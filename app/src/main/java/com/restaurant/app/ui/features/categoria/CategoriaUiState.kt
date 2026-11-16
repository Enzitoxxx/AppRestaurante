package com.restaurant.app.ui.features.categoria

data class CategoriaUi(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val cantidadProductos: Int
)

data class CategoriaUiState(
    val categorias: List<CategoriaUi> = emptyList(),
    val estaCargando: Boolean = false,
    val categoriaADarDeBaja: CategoriaUi? = null,
    val snackbarMensaje: String? = null
) {
    val totalCategorias: Int get() = categorias.size
}
