package com.restaurant.app.ui.features.forgotpassword

// Eventos puntuales transmitidos desde el ViewModel a la UI.
// Este archivo sirve para avisarle a la pantalla que ocurrio algo

sealed interface ForgotPasswordEvent {
    data object ResetPasswordEmailSent : ForgotPasswordEvent
}

// sealed limita los eventos permitidos.
// Ejemplo: solo definimos eventos como "correo enviado" o "error al enviar",
// evitando que se implementen eventos nuevos desde cualquier archivo.