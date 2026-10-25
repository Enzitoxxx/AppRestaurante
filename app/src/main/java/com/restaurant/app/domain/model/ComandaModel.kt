package com.restaurant.app.domain.model

enum class EstadoComanda {
    EN_PREPARACION,
    ENTREGADO,
    COBRADO
}

data class ComandaModel(
    val id: String,              // ej: "124"
    val numeroMesa: String,      // ej: "03"
    val camareroNombre: String,  // ej: "Camila Gómez"
    val cantidadItems: Int,      // ej: 3
    val hora: String,            // ej: "10:25"
    val total: Double,           // ej: 28600.0
    val estado: EstadoComanda
)