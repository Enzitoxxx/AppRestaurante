package com.restaurant.app.data.repository

import com.restaurant.app.data.local.dao.ProductoDao
import com.restaurant.app.data.local.entity.ProductoEntity
import com.restaurant.app.data.local.mapper.toDomain
import com.restaurant.app.data.local.mapper.toEntity
import com.restaurant.app.domain.model.Producto
import com.restaurant.app.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.map

/*
 * IMPLEMENTACIÓN CONCRETA DEL REPOSITORIO DE PRODUCTOS (Capa data/repository).
 *
 * EXPLICACIÓN:
 *
 * 1. FLUJO COMPLETO DE DATOS:
 *    UI (CartaScreen / ProductoScreen)
 *       ↓
 *    ViewModel (CartaViewModel / ProductoViewModel)
 *       ↓
 *    ProductoRepository (Interfaz definida en domain/repository)
 *       ↓
 *    ProductoRepositoryDatos (Implementación en data/repository)
 *       ↓
 *    ProductoDao (Data Access Object en data/local/dao)
 *       ↓
 *    Base de datos SQLite local (Room).
 *
 * 2. INTEGRACIÓN CON RETROFIT / REMOTE:
 *    En una arquitectura completa de producción, este repositorio combinará la fuente de datos
 *    local (Room) con la remota (Retrofit): por ejemplo, solicitando los productos a la API de red
 *    y guardándolos en Room como caché local para permitir el funcionamiento offline.
 */
class ProductoRepositoryDatos(
    private val productoDao: ProductoDao
) : ProductoRepository {

    // Inicialización síncrona de los productos de prueba si la tabla está vacía en Room
    private suspend fun asegurarProductosIniciales() {
        if (productoDao.contar() == 0) {
            val productosIniciales = listOf(
                ProductoEntity(
                    id = "1",
                    nombre = "Empanadas de carne",
                    categoria = "Entradas",
                    area = "Cocina",
                    precio = 2200.0,
                    estadoDisponible = true,
                    descripcion = "Carne cortada a cuchillo con cebolla de verdeo, huevo y aceitunas."
                ),
                ProductoEntity(
                    id = "2",
                    nombre = "Bife de chorizo con papas",
                    categoria = "Platos principales",
                    area = "Cocina",
                    precio = 14800.0,
                    estadoDisponible = true,
                    descripcion = "Carne de novillo a la parrilla con papas fritas provenzal."
                ),
                ProductoEntity(
                    id = "3",
                    nombre = "Cerveza IPA Artesanal",
                    categoria = "Bebidas",
                    area = "Bar",
                    precio = 3500.0,
                    estadoDisponible = false,
                    estadoTexto = "Pausado por stock",
                    descripcion = "Cerveza artesanal tirada de 500cc."
                ),
                ProductoEntity(
                    id = "4",
                    nombre = "Flan casero mixto",
                    categoria = "Postres",
                    area = "Cocina",
                    precio = 3200.0,
                    estadoDisponible = true,
                    descripcion = "Receta tradicional con dulce de leche y crema."
                )
            )
            productosIniciales.forEach { productoDao.guardar(it) }
        }
    }

    override fun observarProductos(): Flow<List<Producto>> =
        productoDao.observarTodos()
            .onStart { asegurarProductosIniciales() }
            .map { lista -> lista.map { it.toDomain() } }

    override suspend fun obtenerProducto(id: String): Producto? {
        asegurarProductosIniciales()
        return productoDao.obtenerPorId(id)?.toDomain()
    }

    override suspend fun guardarProducto(producto: Producto) {
        asegurarProductosIniciales()
        productoDao.guardar(producto.toEntity())
    }

    override suspend fun eliminarProducto(id: String) {
        productoDao.eliminar(id)
    }
}
