package com.restaurant.app.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {

    @Serializable
    data object Splash : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Registro : Screen //Le dice a la app que existe una pantalla mas, en este caso la pantalla del registro

    @Serializable
    data object ForgotPassword : Screen
    @Serializable
    data object Home : Screen   // provisorio, hasta armar el panel del administrador

    @Serializable
    data object Carta : Screen

    /*
       Si productoId == null ➔ Modo Alta ("Nuevo producto").
       Si productoId trae un ID ➔ Modo Edición ("Editar producto").
    */
    @Serializable
    data class ProductoForm(
        val productoId: String? = null
    ) : Screen

    @Serializable
    data object Salon : Screen
}

/*
Este archivo define las pantalas a las que podemos navegar dentro de
la aplicacion.
EJ: iniciar en Splash, pasar al login y despues ir al panel principal

Dentro declaramos las pantallas:
@Serializable
data object Splash : Screen

Esto crea una pantalla identificada como Splash. (lo mismo ocurre con las demas)
- Por que data object?
porque cada pantalla se representa con un objeto unico.


"NavKey" Screen adopta el contrado de NavKey.
para que? para que navigation 3 reconozca los objetos, Spash, Login,
Home y los que pongamos alli, como identificadores validos para navegar


Screen.kt identifica las pantallas disponibles;
AppNavHost.kt define qué mostrar y cómo navegar entre ellas. Son partes distintas que trabajan juntas.
*/