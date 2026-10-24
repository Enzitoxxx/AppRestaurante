package com.restaurant.app.ui.features.personal.form

import androidx.annotation.StringRes

data class EmpleadoFormUiState(
    // Campos del formulario
    val nombre: String = "",
    val telefono: String = "",
    val correo: String = "",
    val rol: String = "Camarero / Mozo", // "Camarero / Mozo", "Cocinero", "Bartender", "Administrador"
    val turno: String = "Turno Mañana", // "Turno Mañana", "Turno Tarde", "Turno Noche", "Turno Cortado"
    val asignacion: String = "Salón Principal", // "Salón Principal", "Patio / Terraza", "Barra", "Cocina"

    // Credenciales temporales generadas automáticamente
    val usuarioGenerado: String = "",
    val claveTemporalGenerada: String = "",

    // Estados de UI
    val esEdicion: Boolean = false,
    val estaCargando: Boolean = false,

    // Errores de validación
    @StringRes val errorNombre: Int? = null,
    @StringRes val errorTelefono: Int? = null,
    @StringRes val errorCorreo: Int? = null
)
