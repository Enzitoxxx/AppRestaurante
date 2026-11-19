package com.restaurant.app.data

import android.content.Context
import com.restaurant.app.data.local.database.AppDatabase
import com.restaurant.app.data.local.sesion.SesionPreferencias
import com.restaurant.app.data.repository.AuthRepositoryDatos
import com.restaurant.app.data.repository.MesaRepositoryDatos
import com.restaurant.app.data.repository.PedidoRepositoryDatos
import com.restaurant.app.data.repository.PersonalRepositoryDatos
import com.restaurant.app.data.repository.ProductoRepositoryDatos
import com.restaurant.app.domain.repository.AuthRepository
import com.restaurant.app.domain.repository.MesaRepository
import com.restaurant.app.domain.repository.PedidoRepository
import com.restaurant.app.domain.repository.PersonalRepository
import com.restaurant.app.domain.repository.ProductoRepository

/*
 * SERVICE LOCATOR (object Repositorios).
 *
 * En esta aplicación utilizamos el patrón Service Locator mediante un `object` de Kotlin.
 *
 * `Repositorios` expone instancias Singleton de cada repositorio (usando `by lazy` para inicializarlos
 * solo cuando se utilicen por primera vez). Se inicializa una vez desde `RestauranteApp` recibiendo el Context.
 */
object Repositorios {

    private lateinit var appContext: Context

    /**
     * Inicializa el Service Locator guardando el ApplicationContext.
     */
    fun iniciar(context: Context) {
        appContext = context.applicationContext
    }

    private val database: AppDatabase by lazy {
        check(::appContext.isInitialized) { "Repositorios.iniciar(context) debe llamarse en RestauranteApp antes de acceder a la capa de datos." }
        AppDatabase.obtenerInstancia(appContext)
    }

    private val sesionPreferencias: SesionPreferencias by lazy {
        check(::appContext.isInitialized) { "Repositorios.iniciar(context) debe llamarse en RestauranteApp antes de acceder a la capa de datos." }
        SesionPreferencias(appContext)
    }

    val auth: AuthRepository by lazy {
        AuthRepositoryDatos(database.usuarioDao(), sesionPreferencias)
    }

    val productos: ProductoRepository by lazy { ProductoRepositoryDatos(database.productoDao()) }
    val mesas: MesaRepository by lazy { MesaRepositoryDatos() }
    val personal: PersonalRepository by lazy { PersonalRepositoryDatos() }
    val pedidos: PedidoRepository by lazy { PedidoRepositoryDatos() }
}
