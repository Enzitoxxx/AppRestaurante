package com.restaurant.app.core.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavegacionItem(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    data object Inicio : NavegacionItem("inicio", "Inicio", Icons.Outlined.Home)
    data object Salon : NavegacionItem("salon", "Salón", Icons.Outlined.Chair)
    data object Carta : NavegacionItem("carta", "Carta", Icons.Outlined.MenuBook)
    data object Personal : NavegacionItem("personal", "Personal", Icons.Outlined.Badge)
    data object Mas : NavegacionItem("mas", "Más", Icons.Outlined.MoreHoriz)

    companion object {
        val items = listOf(Inicio, Salon, Carta, Personal, Mas)
    }
}