package com.restaurant.app.ui.features.producto

import androidx.annotation.StringRes

data class ProductoUiState (

    //Campos que va completar el usuario
    val nombre : String = "",
    val descripcion : String = "",
    val precio : String = "",
    val categoria : String = "Entradas", // "Entradas", "Platos principales", "Bebidas", "Postres"
    val areaPreparacion : String = "Cocina", // Cocina o Barra
    val disponibleParaVenta : Boolean = true,

    //Control de la interrfaz UI
    val esEdicion : Boolean = false, //False = nuevo producto / true = Editar Producto
    val estaCargando : Boolean = false,

    //Mensaje de error de validacion
    @StringRes val errorNombre : Int? = null,
    @StringRes val errorPrecio: Int? = null
)