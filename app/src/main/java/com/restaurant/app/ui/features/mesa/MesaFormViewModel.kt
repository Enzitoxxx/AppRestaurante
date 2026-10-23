package com.restaurant.app.ui.features.mesa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import  com.restaurant.app.R


/*
    1.Permite cambiar el número o código de la mesa.
    2.Incrementa (+) y decrementa (-) la capacidad de comensales (con un límite mínimo de 1 persona).
    3.Selecciona la zona (Salón o Patio).
    4.Precarga los datos existentes si se entra en modo Edición.
    5.Valida que el número de mesa no esté vacío al presionar "Guardar mesa".
*/
class MesaFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MesaFormUiState())
    val uiState : StateFlow<MesaFormUiState> = _uiState.asStateFlow()

    private val _events = Channel<MesaFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    //Aquii precargamos los datos para el modo Edicion
    fun cargarMesaParaEdicion(numero : String, capacidad : Int, zona: String){
        _uiState.update {
            it.copy(
                numeroOMesaCodigo = numero,
                capacidadPersonas = capacidad,
                zonaSeleccionada = zona,
                esEdicion = true
            )
        }
    }

    //Funciones para actualizar el estado segun interaccion del usuario.

    fun onNumeroChange(value: String) {
        _uiState.update { it.copy(numeroOMesaCodigo = value, errorNumero = null) }
    }

    fun onIncrementarCapacidad() {
        _uiState.update { it.copy(capacidadPersonas = it.capacidadPersonas + 1) }
    }

    fun onDecrementarCapacidad() {
        _uiState.update {
            val nuevaCapacidad = if (it.capacidadPersonas > 1) it.capacidadPersonas - 1 else 1
            it.copy(capacidadPersonas = nuevaCapacidad)
        }
    }

    fun onZonaChange(zona: String) {
        _uiState.update { it.copy(zonaSeleccionada = zona) }
    }

    fun onVolverClick() {
        _events.trySend(MesaFormEvent.Volver)
    }

    fun onGuardarClick() {
        val state = _uiState.value
        if (state.estaCargando) return

        val numero = state.numeroOMesaCodigo.trim()

        // Validación: El número o código de mesa es obligatorio
        val errorNumero = if (numero.isBlank()) R.string.mesa_form_error_numero_requerido else null

        if (errorNumero != null) {
            _uiState.update { it.copy(errorNumero = errorNumero) }
            return
        }

        // Simulamos el guardado en segundo plano (asíncrono)
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            delay(1000) // Simulación de llamada a BD / servidor
            _uiState.update { it.copy(estaCargando = false) }
            _events.send(MesaFormEvent.MesaGuardada)
        }
    }

}