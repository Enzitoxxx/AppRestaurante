package com.restaurant.app.data

import com.restaurant.app.data.repository.MesaRepositoryDatos
import com.restaurant.app.data.repository.PersonalRepositoryDatos
import com.restaurant.app.data.repository.ProductoRepositoryDatos
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PersonalRepository
import com.restaurant.app.domain.repository.ProductoRepository

/*
 * Un object tiene UNA sola instancia en toda la app.
 * Acá se crea cada repositorio una vez, y todas las pantallas comparten los mismos datos.
 */
object Repositorios {
    val productos: ProductoRepository by lazy { ProductoRepositoryDatos() }
    val mesas: MesaRepository by lazy { MesaRepositoryDatos() }
    val personal: PersonalRepository by lazy { PersonalRepositoryDatos() }
}