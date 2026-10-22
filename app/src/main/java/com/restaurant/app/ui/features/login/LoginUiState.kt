package com.restaurant.app.ui.features.login

import androidx.annotation.StringRes

data class LoginUiState(

    //Lo que escribe el usuario
    val email: String = "",
    val password: String = "",


    //Estado de la interfaz
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    //Errores de los campos
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null
)