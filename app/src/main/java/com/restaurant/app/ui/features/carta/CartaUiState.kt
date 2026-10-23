package com.restaurant.app.ui.features.carta

import com.restaurant.app.domain.model.Producto

data class CartaUiState(
    val busqueda: String = "",
    val categoriaSeleccionada: String = "Todas", // Todas, Entradas, Platos, Bebidas, Postres
    val productos: List<Producto> = emptyList(),
    val estaCargando: Boolean = false,
    val productoADarDeBaja: Producto? = null // Si no es null, se abre el AlertDialog de confirmación
) {
    // Listado filtrado automáticamente según la búsqueda y la categoría seleccionada
    val productosFiltrados: List<Producto>
        get() = productos.filter { producto ->
            val coincideCategoria = categoriaSeleccionada == "Todas" ||
                    producto.categoria.contains(categoriaSeleccionada, ignoreCase = true)

            val coincideBusqueda = busqueda.isBlank() ||
                    producto.nombre.contains(busqueda, ignoreCase = true) ||
                    producto.categoria.contains(busqueda, ignoreCase = true)

            coincideCategoria && coincideBusqueda
        }
}
