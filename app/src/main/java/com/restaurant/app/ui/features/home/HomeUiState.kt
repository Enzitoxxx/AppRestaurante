package com.restaurant.app.ui.features.home

/** TODO: pasar a domain/data cuando exista el módulo/repositorio de insumos */
data class InsumoAlertaUi(
    val id: String,
    val nombre: String,
    val unidadesRestantes: Int
)

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
    val inicialesUsuario: String = "LF",
    val fechaHoy: String = "",
    val totalVentasHoy: Double = 0.0,
    val porcentajeComparativa: String? = null,
    val totalTransacciones: Int = 0,
    val pedidosAbiertos: Int = 0,
    val mesasOcupadas: Int = 0,
    val totalMesas: Int = 0,
    val topPlatos: List<TopPlatoModel> = emptyList(),
    val alertasStock: List<InsumoAlertaUi> = listOf(
        InsumoAlertaUi("1", "Harina 0000", 3),
        InsumoAlertaUi("2", "Queso Mozzarella", 2),
        InsumoAlertaUi("3", "Café en grano", 1)
    )
) {
    val porcentajeOcupacion: Int
        get() = if (totalMesas > 0) (mesasOcupadas * 100) / totalMesas else 0
}
