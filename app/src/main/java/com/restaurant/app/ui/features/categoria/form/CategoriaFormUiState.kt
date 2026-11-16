package com.restaurant.app.ui.features.categoria.form

import androidx.annotation.StringRes

data class CategoriaFormUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val estaCargando: Boolean = false,
    @StringRes val nombreError: Int? = null,
    val esEdicion: Boolean = false
)
