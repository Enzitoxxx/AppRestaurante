package com.restaurant.app.ui.features.mesa

import androidx.annotation.StringRes

data class MesaFormUiState (

    //Campos del formulario
    val numeroOMesaCodigo : String = "", // Ejemplo: mesa 07
    val capacidadPersonas : Int = 4, // Ej: 4 clientes
    val zonaSeleccionada : String = "Salon", // Salon o Patio

    //Estados de la Interfaz UI
    val esEdicion : Boolean = false,
    val estaCargando : Boolean = false,

    //Errores de validacion
    @StringRes val errorNumero : Int? = null
)