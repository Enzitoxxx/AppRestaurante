package com.restaurant.app.ui.features.home

/** Modelo para representar cada fila en el ranking de Top Ventas. */
data class TopPlatoModel(
    val ranking: Int,
    val nombre: String,
    val cantidadVendida: Int,
    val totalRecaudado: Double
)

/** Estado observable de la pantalla principal / Dashboard del Administrador. */
data class HomeUiState(
    val nombreRestaurante: String = "La Buena Mesa",
    val fechaHoy: String = "",
    val totalVentasHoy: Double = 284500.0,
    val porcentajeComparativa: String = "+18% vs lunes",
    val totalTransacciones: Int = 42,
    val pedidosAbiertos: Int = 8,
    val mesasOcupadas: Int = 6,
    val totalMesas: Int = 10,
    val topPlatos: List<TopPlatoModel> = listOf(
        TopPlatoModel(1, "Empanadas de carne", 34, 74800.0),
        TopPlatoModel(2, "Bife de chorizo con papas", 18, 266400.0),
        TopPlatoModel(3, "Cerveza IPA Artesanal", 25, 87500.0)
    )
) {
    val porcentajeOcupacion: Int
        get() = if (totalMesas > 0) (mesasOcupadas * 100) / totalMesas else 0
}