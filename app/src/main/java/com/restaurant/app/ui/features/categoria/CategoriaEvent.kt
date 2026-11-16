package com.restaurant.app.ui.features.categoria

sealed interface CategoriaEvent {
    data class MostrarSnackbar(val mensaje: String) : CategoriaEvent
}
