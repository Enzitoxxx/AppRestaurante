package com.restaurant.app.data.repository

import com.restaurant.app.data.datasource.local.dao.ProductoDao
import com.restaurant.app.data.datasource.local.entity.ProductoEntity
import com.restaurant.app.domain.model.Producto
import com.restaurant.app.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
 * IMPLEMENTACIÓN CONCRETA DEL REPOSITORIO DE PRODUCTOS (Capa data/repository).
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
 *    ProductoDao (Data Access Object en data/datasource/local/dao)
 *       ↓
 *    Base de datos SQLite local (Room).
 */
class ProductoRepositoryDatos(
    private val productoDao: ProductoDao
) : ProductoRepository {

    override fun observarProductos(): Flow<List<Producto>> =
        productoDao.observarTodos().map { lista ->
            lista.map { it.toDomain() }
        }

    override suspend fun obtenerProducto(id: String): Producto? {
        return productoDao.obtenerPorId(id)?.toDomain()
    }

    override suspend fun guardarProducto(producto: Producto) {
        productoDao.guardar(ProductoEntity.fromDomain(producto))
    }

    override suspend fun eliminarProducto(id: String) {
        productoDao.eliminar(id)
    }
}
