package com.restaurant.app.ui.features.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeUiState(
            fechaHoy = obtenerFechaActualFormateada()
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /*Genera automaticamente la fecha de hoy*/
    private fun obtenerFechaActualFormateada(): String {
        val formatter = SimpleDateFormat("d 'de' MMM. yyyy", Locale("es", "AR"))
        return formatter.format(Date())
    }
}


    /* ViewModel --> encargado de manejar los datos y logica de la pantalla
     Ej: En la pantalla home necesitamos mostrar el nombre, fecha, ventas del dia, etc, del restaurante.
     El HomeUiState contiene esos datos completos y el HomeViewModel se encarga de
     preparalos y actualizarlos.
     HomeScreen: muestra la pantalla.
     HomeUiState: guarda los datos que necesita mostrar.
     HomeViewModel: administra esos datos.

     _uiState --> es un estado interno que el ViewModel puede modificar

     MutableStateFlow --> es una herramienta que permite guardar un valor y
     modificarlo, notificando a quienes lo estan observando cuando cambia.
     Ej: Si tenemos 8 pedidos y despues pasan a ser 9, se podria actualizar el estado
     y la pantalla recibira el cambio.

     StateFlow --> Permite que otras partes del programa observen el estado y
     reciban sus cambios.
     En nuestro caso, "StateFlow<HomeUiState> significa que el flujo contiene objetos
     del tipo HomeUiState.
     ¿Por que no usamos directamente _uiState?
     porque queremos separar quien puede modificar los datos y quien solo puede verlos (observarlos)
     * _uiState: permite modificar el estado
     * uiState: permite observarlo, pero no modificarlo

     asStateFlow --> sirve para que la pantalla pueda leer los datos, pero no modificarlos directamente
     */


