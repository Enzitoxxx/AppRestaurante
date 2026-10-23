package com.restaurant.app.ui.features.salon

import com.restaurant.app.data.model.EstadoMesa
import com.restaurant.app.data.model.Mesa

data class SalonUiState(
    val zonaSeleccionada: String = "Todas", // "Todas", "Salón", "Patio"
    val mesas: List<Mesa> = emptyList(),
    val estaCargando: Boolean = false,
    val mesaADarDeBaja: Mesa? = null // Si no es null, abre el AlertDialog de confirmación
) {
    // Totales calculados automáticamente para el resumen superior
    val ocupadasCount: Int get() = mesas.count { it.estado == EstadoMesa.OCUPADA }
    val libresCount: Int get() = mesas.count { it.estado == EstadoMesa.LIBRE }
    val cuentaCount: Int get() = mesas.count { it.estado == EstadoMesa.CUENTA }

    // Mesas filtradas por la zona seleccionada
    val mesasFiltradas: List<Mesa>
        get() = if (zonaSeleccionada.equals("Todas", ignoreCase = true)) mesas
        else mesas.filter { it.zona.equals(zonaSeleccionada, ignoreCase = true) }
}
