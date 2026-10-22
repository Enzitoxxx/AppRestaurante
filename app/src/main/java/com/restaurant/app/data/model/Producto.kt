package com.restaurant.app.data.model

//En esta clase vamos a tener todos los productos que van a estar en la carta
//Ejemplo: Entradas, Plato principal, bebidas, postres, etc
data class Producto (
    val id : String,
    val nombre : String, //Nombre del producto
    val categoria : String, // Entradas, Plato principal, bebidas, postres
    val area : String, // bar, cocina
    val precio : Double,
    val estadoDisponible : Boolean,
    val estadoTexto : String? = null // "Ej: Pausado por stock"

)