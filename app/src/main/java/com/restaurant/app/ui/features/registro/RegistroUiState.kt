package com.restaurant.app.ui.features.registro

import androidx.annotation.StringRes

data class RegistroUiState(
    //Lo que escribe el usuario
    val nombre: String = "",
    val correo : String = "",
    val telefono : String = "",
    val restaurante : String = "",
    val contrasena : String = "",

    //El estado de la interfaz
    val contrasenaVisible : Boolean = false,
    val estaCargado : Boolean = false,

    //Los errores de cada campo
    @StringRes val errorNombre: Int? = null,
    @StringRes val errorCorreo: Int? = null,
    @StringRes val errorTelefono: Int? = null,
    @StringRes val errorRestaurante: Int? = null,
    @StringRes val errorContrasena: Int? = null
)



