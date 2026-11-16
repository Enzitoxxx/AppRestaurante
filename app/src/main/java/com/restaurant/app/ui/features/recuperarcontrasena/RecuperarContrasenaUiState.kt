package com.restaurant.app.ui.features.recuperarcontrasena

import androidx.annotation.StringRes

// Guarda el correo, el estado de carga y posibles errores de validación

data class RecuperarContrasenaUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val mostrarMensajeExito: Boolean = false,
    @StringRes val emailError: Int? = null
)
