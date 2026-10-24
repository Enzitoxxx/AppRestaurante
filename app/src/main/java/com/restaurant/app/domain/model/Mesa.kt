package com.restaurant.app.domain.model

enum class EstadoMesa {
    LIBRE,      // Verde
    OCUPADA,    // Rojo / Rosa
    CUENTA      // Amarillo / Naranja
}

data class Mesa(
    val id: String,
    val numero: Int,
    val zona: String,
    val capacidadPersonas: Int,
    val estado: EstadoMesa,
    val empleadoAsignadoId: String? = null,
    val consumoTotal: Double? = null,
    val detalleEstado: String? = null
) {
    // Para que muestre -> "Mesa 01"
    val nombre: String
        get() = "Mesa ${numero.toString().padStart(2, '0')}"
}