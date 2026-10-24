package com.restaurant.app.domain.model

data class Empleado(
    val id: String,
    val nombre: String,
    val rol: String,           // Ej: Cocinero, Camarero/Mozo, Bartender
    val turno: String,         // Ej: Turno Mañana, Turno Tarde, Turno Noche, Turno Cortado
    val asignacion: String,    // Ej: Salón Principal, Patio / Terraza, Barra, Cocina
    val estaActivo: Boolean = true,
    val telefono : String = "",
    val correo : String = ""
) {
    // Propiedad calculada para obtener las iniciales del nombre (Ej: Enzo Vallejos -> EV)
    val iniciales: String
        get() {
            val partes = nombre.trim().split(" ")
            return if (partes.size >= 2) {
                "${partes[0].take(1)}${partes[1].take(1)}".uppercase()
            } else {
                nombre.take(2).uppercase()
            }
        }

    // "Lisandro Araoz" -> "Lizandro A." (para tarjetas con poco espacio)
    val nombreCorto: String
        get() {
            val partes = nombre.trim().split(" ")
            return if (partes.size >= 2) "${partes[0]} ${partes[1].take(1)}." else nombre
        }

    // Zona de mesas : "Salon" o "Patio".
    // Es null si no trabaja con mesas (Barra, Cocina): en ese caso no se filtra.
    val zonaMesas: String?
        get() = when {
            asignacion.contains("Patio", ignoreCase = true) -> "Patio"
            asignacion.contains("Salón", ignoreCase = true) ||
                    asignacion.contains("Salon", ignoreCase = true) -> "Salon"
            else -> null
        }

    // true si el rol atiende mesas (Camarero / Mozo)
    val atiendeMesas: Boolean
        get() = rol.contains("Camarer", ignoreCase = true) ||
                rol.contains("Mozo", ignoreCase = true)
}
