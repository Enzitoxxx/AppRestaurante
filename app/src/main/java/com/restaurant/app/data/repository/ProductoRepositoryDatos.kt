package com.restaurant.app.data.repository

import com.restaurant.app.domain.model.Producto
import com.restaurant.app.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductoRepositoryDatos : ProductoRepository {

    // La lista vive acá. Es un StateFlow: cuando cambia, avisa a quien la esté mirando.
    private val productos = MutableStateFlow(
        listOf(
            Producto(
                id = "1",
                nombre = "Empanadas de carne",
                categoria = "Entradas",
                area = "Cocina",
                precio = 2200.0,
                estadoDisponible = true,
                descripcion = "Carne cortada a cuchillo con cebolla de verdeo, huevo y aceitunas."
            ),
            Producto(
                id = "2",
                nombre = "Bife de chorizo con papas",
                categoria = "Platos principales",
                area = "Cocina",
                precio = 14800.0,
                estadoDisponible = true,
                descripcion = "Carne cortada a cuchillo con cebolla de verdeo, huevo y aceitunas."
            ),
            Producto(
                id = "3",
                nombre = "Cerveza IPA Artesanal",
                categoria = "Bebidas",
                area = "Bar",
                precio = 3500.0,
                estadoDisponible = false,
                estadoTexto = "Pausado por stock",
                descripcion = "Carne cortada a cuchillo con cebolla de verdeo, huevo y aceitunas."
            ),
            Producto(
                id = "4",
                nombre = "Flan casero mixto",
                categoria = "Postres",
                area = "Cocina",
                precio = 3200.0,
                estadoDisponible = true,
                descripcion = "Carne cortada a cuchillo con cebolla de verdeo, huevo y aceitunas."
            )
        )
    )

    override fun observarProductos(): Flow<List<Producto>> = productos.asStateFlow()

    override suspend fun obtenerProducto(id: String): Producto? =
        productos.value.find { it.id == id }

    override suspend fun guardarProducto(producto: Producto) {
        productos.update { lista ->
            if (lista.any { it.id == producto.id }) {
                // Ya existe: lo reemplazamos (edición, activar o pausar)
                lista.map { if (it.id == producto.id) producto else it }
            } else {
                // No existe: lo agregamos al final (alta)
                lista + producto
            }
        }
    }

    override suspend fun eliminarProducto(id: String) {
        productos.update { lista -> lista.filterNot { it.id == id } }
    }
}