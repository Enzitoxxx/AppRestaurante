package com.restaurant.app.ui.features.forgotpassword

import androidx.annotation.StringRes

// Guarda el correo, el estado de carga y posibles errores de validacion

data class ForgotPasswordUiState (
    val email: String = "",
    val isLoading: Boolean = false,
    @StringRes val emailError: Int? = null
)
// @StringRes indica que el Int guarda el identificador de un texto de strings.xml

