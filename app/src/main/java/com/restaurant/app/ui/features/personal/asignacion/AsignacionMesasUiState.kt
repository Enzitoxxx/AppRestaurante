package com.restaurant.app.ui.features.personal.asignacion

import com.restaurant.app.domain.model.Mesa

/**
 * ESTADO DE LA PANTALLA DE ASIGNACIÓN DE MESAS.
 *
 * Mantiene la foto instantánea e inmutable de la UI:
 * - Informacion del empleado (nombre y rol).
 * - Lista de mesas disponibles para seleccionar.
 * - Set de IDs de las mesas que han sido marcadas/seleccionadas con el Checkbox.
 * - Estado de carga mientras se guarda la asignación.
 */
data class AsignacionMesasUiState(
    // Datos del empleado/camarero a quien se le asignan las mesas
    val nombreEmpleado: String = "Camila Gómez",
    val rolEmpleado: String = "Camarera",

    // Lista de todas las mesas cargadas desde el repositorio (MesaRepository)
    val mesasDisponibles: List<Mesa> = emptyList(),

    // Set de IDs de las mesas seleccionadas (usamos Set para evitar duplicados y búsquedas rápidas)
    val idsMesasSeleccionadas: Set<String> = setOf("1", "2", "3"), // Por defecto preseleccionamos Mesas 01, 02 y 03

    // Estado de carga mientras se simula el guardado
    val estaCargando: Boolean = false
) {
    /*
     * Propiedad calculada: Retorna la cantidad total de mesas seleccionadas.
     * Se usa para actualizar dinámicamente el texto del botón: "Confirmar asignación (3 mesas)".
     */
    val totalSeleccionadas: Int get() = idsMesasSeleccionadas.size
}
