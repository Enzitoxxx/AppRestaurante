package com.restaurant.app.ui.features.personal.asignacion

/**
 * EVENTOS ÚNICOS DE NAVEGACIÓN (One-Shot Events).
 *
 * Emitidos por el ViewModel hacia la Screen cuando ocurren acciones puntuales
 * que no deben repetirse al rotar la pantalla:
 * - AsignacionGuardada: Notifica que se guardaron las mesas a cargo y debe volver atrás.
 * - Volver: Notifica que se presionó el botón '←' para regresar.
 */
sealed interface AsignacionMesasEvent {
    data object AsignacionGuardada : AsignacionMesasEvent
    data object Volver : AsignacionMesasEvent
}
