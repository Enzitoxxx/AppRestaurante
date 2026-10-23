package com.restaurant.app.domain.model

enum class EstadoMesa {
    LIBRE,      // Verde
    OCUPADA,    // Rojo / Rosa
    CUENTA      // Amarillo / Naranja
}

data class Mesa(
    val id: String,
    val numero: String,             // Ej: "Mesa 01"
    val zona: String,               // Ej: "Salón", "Patio"
    val capacidadPersonas: Int,     // Ej: 4
    val estado: EstadoMesa,         // LIBRE, OCUPADA, CUENTA
    val mozoAsignado: String? = null, // Ej: "Camila G."
    val consumoTotal: Double? = null, // Ej: 18200.0
    val detalleEstado: String? = null // Ej: "Sin comanda", "Esperando ticket"
)