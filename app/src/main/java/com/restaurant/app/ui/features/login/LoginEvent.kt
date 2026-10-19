package com.restaurant.app.ui.features.login

// Evento de una sola vez que el ViewModel le manda a la UI.
sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
}