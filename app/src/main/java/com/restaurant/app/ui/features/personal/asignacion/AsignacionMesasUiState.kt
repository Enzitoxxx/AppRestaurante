package com.restaurant.app.ui.features.personal.asignacion

import com.restaurant.app.domain.model.Mesa

/*
 * ESTADO DE LA PANTALLA DE ASIGNACIÓN DE MESAS.
 * - Datos del empleado y lista de mesas.
 * - Nombres de los empleados, para saber a quién pertenece cada mesa asignada.
 * - Set de IDs de las mesas marcadas con el Checkbox.
 */
data class AsignacionMesasUiState(
    val idEmpleado: String = "",
    val zonaMesas: String? = null, // Salon , patio o nul
    val nombreEmpleado: String = "",
    val rolEmpleado: String = "",
    val mesasDisponibles: List<Mesa> = emptyList(),
    val nombresEmpleados: Map<String, String> = emptyMap(),   // id -> nombre corto
    val idsMesasSeleccionadas: Set<String> = emptySet(),
    val estaCargando: Boolean = false
) {
    val totalSeleccionadas: Int get() = idsMesasSeleccionadas.size

    // Solo las mesas de la zona del empleado (si no tiene zona definida, se muestran todas)
    val mesasVisibles: List<Mesa>
        get() = if (zonaMesas == null) mesasDisponibles
        else mesasDisponibles.filter { it.zona.equals(zonaMesas, ignoreCase = true) }

    /*
     * Si la mesa la tiene ya  OTRO camarero, devuelve el nombre de ese camarero.
     * Si está libre de asignación o es de este mismo empleado, devuelve null.
     */
    fun asignadaAOtro(mesa: Mesa): String? {
        val dueno = mesa.empleadoAsignadoId ?: return null
        if (dueno == idEmpleado) return null
        return nombresEmpleados[dueno] ?: "otro camarero"
    }
}