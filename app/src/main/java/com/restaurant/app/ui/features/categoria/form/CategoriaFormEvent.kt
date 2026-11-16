package com.restaurant.app.ui.features.categoria.form

sealed interface CategoriaFormEvent {
    data object CategoriaGuardada : CategoriaFormEvent
    data object Volver : CategoriaFormEvent
}
