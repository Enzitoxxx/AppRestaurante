package com.restaurant.app.ui.features.registro


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


/*
    Se encarga de mantener y actualizar el RegistroUiState a medida que el usuario vaya escribiendo en cada campo.
    Limpia los errores de cada campo en tiempo real cuando el usuario modificar.
    Valida todos los campos cuando presionemos el "Crear cuenta".
    Emitir el evento RegistroEvent.RegistroSuccess cuando las validaciones sean correctas.
*/
class RegistroViewModel : ViewModel(){


    // ESTADO DE LA PANTALLA (StateFlow):
    // Representa la "foto actual" y continua de la UI (textos, errores, visibilidad de contraseña, etc).
    // - _uiState: Privada y mutable. Solo este ViewModel puede modificar los datos.
    // - uiState: Pública y de solo lectura. La pantalla la observa para redibujar los componentes en tiempo real, Ejemplo cuando gira la pantalla.
    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState : StateFlow<RegistroUiState> = _uiState.asStateFlow() //StateFlow: se acuerdaa exactamente como estaba la pantalla antes de que gire y se vuelve a dibujar

    // EVENTOS DE UNA SOLA VEZ (One-Shot Events / Channel):
    // Representa acciones instantáneas que deben ejecutarse una sola vez (ej: navegar a Home tras registro exitoso).
    // A diferencia del  uiState, estos eventos se consumen y no se repiten cuando la pantalla se gira o redibuja.
    private val _events = Channel<RegistroEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    /*
        Estas funciones son las que llama RegistroForm cada vez que el usuario escribe o borra algo en cada campo.
        El parametro value es el nuevo valor del campo.
        _uiState.update{} -> Es la forma segura de actualizar un MutableStateFlow, dentro de las llaves, la variable "it" representa el estado actual de la pantalla antes del cambio.
        it.copy(...) Usamos este pq RegistroUiState es una data class inmutable, entonces no podemos cambiar directamente sus propiedades.
            Usamos .copy() que nos crea un nuevo estado copiando lo que ya tenia y modifca unicamente 2 cosas (los parametros que le pasamos).
            Ejemplo:
            nombre = value -> Guarda la nueva cadena en el estado para que se dibuje inmediatamente en el cuadro del texto.
            errorNombre = null -> Limpia el error en tiempo real. Si puso "crear cuenta" con el campo vacio le salta el error, en cuanto escriba la primer letra, el mensaje en rojo se borra automaticamente.
    */
    fun onNombreChange(value: String){
        _uiState.update { it.copy(nombre = value, errorNombre = null) }
    }

    fun onCorreoChange(value : String){
        _uiState.update { it.copy(correo = value, errorCorreo= null) }
    }

    fun onTelefonoChange(value: String) {
        _uiState.update { it.copy(telefono = value, errorTelefono = null) }
    }

    fun onRestauranteChange(value: String) {
        _uiState.update { it.copy(restaurante = value, errorRestaurante = null) }
    }

    fun onContrasenaChange(value: String) {
        _uiState.update { it.copy(contrasena = value, errorContrasena = null) }
    }

    fun onToggleContrasenaVisibility() {
        _uiState.update { it.copy(contrasenaVisible = !it.contrasenaVisible) }
    }

    /*
        Esta funcion se ejecuta cuando presionan el botón "Crear cuenta".
        Esta pasa por 4 fases:
        1. Evita el doble clic si ya está cargando.
        2. Limpia los espacios en blanco (.trim()).
        3. Valida cada campo. Si encuentra algún error, actualiza la UI con los mensajes rojos y frena (return).
        4. Si todo está correcto, simula el envío del registro en segundo plano y avisa a la UI que fue exitoso.
    */
    fun onRegistroClick() {
        val state = _uiState.value
        // Fase 1: Evitamos enviar peticiones duplicadas si ya se está procesando
        if (state.estaCargado) return

        // Fase 2: Sacamos los espacios en blanco accidentales al inicio y al final
        val nombre = state.nombre.trim()
        val correo = state.correo.trim()
        val telefono = state.telefono.trim()
        val restaurante = state.restaurante.trim()
        val contrasena = state.contrasena

        // Fase 3: Validamos cada campo según las reglas de negocio
        val errorNombre = if (nombre.isBlank()) R.string.registro_error_nombre_requerido else null

        val errorCorreo = when {
            correo.isBlank() -> R.string.login_error_email_required
            !EMAIL_REGEX.matches(correo) -> R.string.login_error_email_invalid
            else -> null
        }

        val errorTelefono = when {
            telefono.isBlank() -> R.string.registro_error_telefono_requerido
            telefono.count { it.isDigit() } < 8 -> R.string.registro_error_telefono_invalido
            else -> null
        }

        val errorRestaurante = if (restaurante.isBlank()) R.string.registro_error_restaurante_requerido else null

        val errorContrasena = when {
            contrasena.isBlank() -> R.string.login_error_password_required
            contrasena.length < 8 -> R.string.registro_error_contrasena_corta
            else -> null
        }

        // Si al menos un campo tiene error, los mostramos en rojo en la pantalla y frenamos (return)
        if (errorNombre != null || errorCorreo != null || errorTelefono != null ||
            errorRestaurante != null || errorContrasena != null
        ) {
            _uiState.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorCorreo = errorCorreo,
                    errorTelefono = errorTelefono,
                    errorRestaurante = errorRestaurante,
                    errorContrasena = errorContrasena
                )
            }
            return
        }

        // Fase 4: Si todas las validaciones pasaron, procesamos el registro en segundo plano (corrutina)
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargado = true) } // Activa el loader en el botón
            delay(1000) // Simulación de llamada al servidor / API / Base de datos
            _uiState.update { it.copy(estaCargado = false) } // Apaga el loader
            _events.send(RegistroEvent.RegistroSuccess) // Notifica a la pantalla para que navegue
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }

}