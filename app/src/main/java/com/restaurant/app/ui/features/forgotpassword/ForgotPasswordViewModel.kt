package com.restaurant.app.ui.features.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
// Maneja el correo, valida los datos y procesa el boton
// Guardar el correo, validar si es correcto y controlar si esta cargando

// ViewModel encargado de manera la logica de una pantalla.

class ForgotPasswordViewModel : ViewModel() {

    // Estado interno: el ViewModel puede modificarlo
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    // estado publico: la pantalla puede observar los cambios
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    /*
    el ViewModel guarda el estado de la pantalla y permite que
    la pantalla lo observe, pero solo el ViewModel puede modificarlo.
    ** _uiState: estado que el ViewModel puede modifcar
    ** uiState: estado que la pantalla puede observar pero no modifcar directamente

    "val" no se puede reasignar
    "MutableStateFlow" permite guardar un estado y modificarlo
    "ForgotPasswordUiState()" crea el estado inicial de la pantalla
    "StateFlow" permite observar el estado sin modificarlo directamente
    "asStateFlow()" expone el estado como una version de solo lectura

    Ejemplo: Cuando el usuario escribe su correo, el ViewModel modifica _uiState y
    la pantalla observa el cambio mediante uiState.
    */


    //-------
    // Esto sirve para que el VIEWMODEL pueda avisarle algo a la pantalla
    // Canal interno para enviar eventos.
    private val _events =
        Channel<ForgotPasswordEvent>(Channel.BUFFERED)
    //Permite que la pantalla reciba eventos.
    val events = _events.receiveAsFlow()

    /*
    "private" solo se usa dentro del ViewModel
    "_events" es el canal por donde se envian los eventos
    "Channel<ForgotPasswordEvent>" transporta eventos de tipo ForgotPasswordEvent
    "Channel.BUFFERED" permite guardar eventos temporalmente si la pantalla todavia no los recibio
    "receiveAsFlow()" permite que la pantalla reciba los eventos mediante un "Flow"

    y Flow" es como un mecanismo para recibir informacion que va llegando
    */

    // Se ejecuta cuando el usuario cambia el correo
    fun onEmailChange(value: String) {
        //Actualiza el estado con el nuevo correo y borra el error anterior
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    /*
    "value: String" el texto que escribio el usuario
    "_uiState.update" actualiza el estado de la pantalla
    "it.copy()" crea una copia del estado cambiando solamente ls valores indicados
    "email = value" guarda el nuevo correo
    "emailError = null" borra el anterior porque el usuario esta corrigiendo el correo

    Ejemplo: si escribimos un correo incorrecto y aparece un error,
    cuando volvemos a escribir se borra ese error
    */

    fun onSendClick(){
        val state = _uiState.value //obtenemos el estado de la pantalla
        if(state.isLoading) return // si ya esta cargando, return detiene la funcion para evitar otro envio

        val email = state.email.trim()
        val emailError = when {
            email.isBlank() -> R.string.forgot_password_error_email_required!EMAIL_REGEX.matches(email) -> R.string.forgot_password_error_email_invalid else -> null
        }

        // WHEN -> sirve para evaluar varias condiciones y elegir que hacer
        // segun el resultado. Es parecido al if pero permite ordenar varias opciones de arriba
        // a abajo y se queda con la primera que se cumple.

        //Si hay un error, lo guarda en el estado para mostrarlo en pantalla y return detiene la función.
        if(emailError != null){
            _uiState.update { it.copy(emailError = emailError) }
            return
        }

        // Si el correo es valido
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000) // TODO: luego reemplazar por la llamada real a repositorio de autenticacion
            _uiState.update { it.copy(isLoading =  false) }
            _events.send(ForgotPasswordEvent.ResetPasswordEmailSent)
        }

        // "viewModelScope.launch" inicia una tarea que puede ejecutarse de forma asincrona
        // "isLoagin = true" activa el indicador de carga
        // "delay(1000)" espera un segundo
        // "isLoading" desactiva la carga
        // "_events.send(..)" avisa a la pantalla que el proceso termino correctamente

    }

    private companion object{
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }


}