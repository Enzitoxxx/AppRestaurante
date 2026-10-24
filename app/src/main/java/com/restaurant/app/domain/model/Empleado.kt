package com.restaurant.app.domain.model

data class Empleado(
    val id: String,
    val nombre: String,
    val rol: String,           // Ej: Cocinero, Camarero/Mozo, Bartender, Administrador
    val turno: String,         // Ej: Turno Mañana, Turno Tarde, Turno Noche, Turno Cortado
    val asignacion: String,    // Ej: Salón Principal, Patio / Terraza, Barra, Cocina
    val estaActivo: Boolean = true
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
}
