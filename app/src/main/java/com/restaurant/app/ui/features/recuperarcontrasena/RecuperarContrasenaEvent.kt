package com.restaurant.app.ui.features.recuperarcontrasena

// Eventos puntuales transmitidos desde el ViewModel a la UI.
// Este archivo sirve para avisarle a la pantalla que ocurrió algo.

sealed interface RecuperarContrasenaEvent {
    data object ResetPasswordEmailSent : RecuperarContrasenaEvent
}
