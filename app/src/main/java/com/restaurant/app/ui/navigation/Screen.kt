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
    data object Home : Screen   // provisorio, hasta armar el panel del administrador
}