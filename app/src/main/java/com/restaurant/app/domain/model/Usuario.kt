package com.restaurant.app.domain.model

/*
 * Modelo de dominio que representa a un Usuario autenticado en el sistema del restaurante.
 *
 * En la capa de dominio (domain), manejamos solo los datos del usuario que la aplicación
 * necesita para su lógica de negocio y presentación visual. Por razones de seguridad,
 * la contraseña NUNCA debe formar parte del modelo de dominio ni viajar hacia los ViewModels
 * o las pantallas Compose. La contraseña vive únicamente en la capa de datos (data/local en la Entity).
 */
data class Usuario(
    val id: String,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val estaActivo: Boolean,
    val restauranteNombre: String
)
