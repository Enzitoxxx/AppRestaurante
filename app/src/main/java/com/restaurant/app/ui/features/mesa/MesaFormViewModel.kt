package com.restaurant.app.ui.features.mesa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.R
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.model.EstadoMesa
import com.restaurant.app.domain.model.Mesa
import com.restaurant.app.domain.repository.MesaRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/*
    1. Permite cambiar el número de la mesa (solo dígitos).
    2. Incrementa (+) y decrementa (-) la capacidad, con mínimo de 1 persona.
    3. Selecciona la zona (Salón o Patio).
    4. En modo Edición, carga la mesa desde el repositorio.
    5. Valida: número obligatorio, mayor a cero y no repetido (RF09).
    6. Guarda en el repositorio y avisa con MesaFormEvent.
*/
class MesaFormViewModel(
    private val repositorio: MesaRepository = Repositorios.mesas
) : ViewModel() {

    private val _uiState = MutableStateFlow(MesaFormUiState())
    val uiState: StateFlow<MesaFormUiState> = _uiState.asStateFlow()

    private val _events = Channel<MesaFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // null = mesa nueva. Con valor = la mesa que se está editando.
    private var mesaOriginal: Mesa? = null

    // MODO EDICIÓN: busca la mesa en el repositorio y precarga el formulario
    fun cargarMesa(id: String) {
        if (mesaOriginal?.id == id) return   // ya se cargó: no pisar lo que el usuario edita
        viewModelScope.launch {
            val mesa = repositorio.obtenerMesa(id) ?: return@launch
            mesaOriginal = mesa
            _uiState.update {
                it.copy(
                    numeroOMesaCodigo = mesa.numero.toString(),
                    capacidadPersonas = mesa.capacidadPersonas,
                    zonaSeleccionada = mesa.zona,
                    esEdicion = true
                )
            }
        }
    }

    fun onNumeroChange(value: String) {
        // Solo permite ingresar dígitos
        if (value.all { it.isDigit() }) {
            _uiState.update { it.copy(numeroOMesaCodigo = value, errorNumero = null) }
        }
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

        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }

            val texto = state.numeroOMesaCodigo.trim()
            val errorNumero = validarNumero(texto)
            if (errorNumero != null) {
                _uiState.update { it.copy(estaCargando = false, errorNumero = errorNumero) }
                return@launch
            }

            val numero = texto.toInt()
            val original = mesaOriginal

            val mesa = original?.copy(
                // Edición: cambia solo lo del formulario y conserva estado, mozo y consumo
                numero = numero,
                zona = state.zonaSeleccionada,
                capacidadPersonas = state.capacidadPersonas
            ) ?: Mesa(
                // Alta: mesa nueva, libre y con id nuevo
                id = UUID.randomUUID().toString(),
                numero = numero,
                zona = state.zonaSeleccionada,
                capacidadPersonas = state.capacidadPersonas,
                estado = EstadoMesa.LIBRE
            )

            repositorio.guardarMesa(mesa)

            _uiState.update { it.copy(estaCargando = false) }
            _events.send(MesaFormEvent.MesaGuardada)
        }
    }

    // Devuelve el id del texto de error, o null si el número es válido
    private suspend fun validarNumero(texto: String): Int? {
        if (texto.isBlank()) return R.string.mesa_form_error_numero_requerido

        val numero = texto.toIntOrNull()
        if (numero == null || numero <= 0) return R.string.mesa_form_error_numero_mayor_cero

        // No repetido: ninguna OTRA mesa puede tener ese número
        val mesas = repositorio.observarMesas().first()
        val repetido = mesas.any { it.numero == numero && it.id != mesaOriginal?.id }
        return if (repetido) R.string.mesa_form_error_numero_repetido else null
    }
}