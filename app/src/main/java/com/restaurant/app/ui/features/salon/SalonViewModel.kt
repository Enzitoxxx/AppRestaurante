package com.restaurant.app.ui.features.salon

import androidx.lifecycle.ViewModel
import com.restaurant.app.data.model.EstadoMesa
import com.restaurant.app.data.model.Mesa
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class SalonViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SalonUiState())
    val uiState: StateFlow<SalonUiState> = _uiState.asStateFlow()

    private val _events = Channel<SalonEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        cargarMesasFicticias()
    }

    private fun cargarMesasFicticias() {
        val listaMesas = listOf(
            // Salón
            Mesa(
                id = "1",
                numero = "Mesa 01",
                zona = "Salón",
                capacidadPersonas = 4,
                estado = EstadoMesa.LIBRE,
                detalleEstado = "Sin comanda"
            ),
            Mesa(
                id = "2",
                numero = "Mesa 02",
                zona = "Salón",
                capacidadPersonas = 2,
                estado = EstadoMesa.OCUPADA,
                mozoAsignado = "Camila G.",
                consumoTotal = 18200.0
            ),
            Mesa(
                id = "3",
                numero = "Mesa 03",
                zona = "Salón",
                capacidadPersonas = 4,
                estado = EstadoMesa.OCUPADA,
                mozoAsignado = "Camila G.",
                consumoTotal = 28600.0
            ),
            Mesa(
                id = "4",
                numero = "Mesa 04",
                zona = "Salón",
                capacidadPersonas = 4,
                estado = EstadoMesa.CUENTA,
                detalleEstado = "Esperando ticket",
                consumoTotal = 34100.0
            ),

            // Patio
            Mesa(
                id = "5",
                numero = "Mesa 05",
                zona = "Patio",
                capacidadPersonas = 2,
                estado = EstadoMesa.LIBRE
            ),
            Mesa(
                id = "6",
                numero = "Mesa 06",
                zona = "Patio",
                capacidadPersonas = 6,
                estado = EstadoMesa.OCUPADA,
                consumoTotal = 42000.0
            )
        )

        _uiState.update { it.copy(mesas = listaMesas) }
    }

    fun onZonaSeleccionada(zona: String) {
        _uiState.update { it.copy(zonaSeleccionada = zona) }
    }

    fun onMesaClick(mesa: Mesa) {
        _events.trySend(SalonEvent.AbrirMesa(mesa))
    }

    fun onNuevaMesaClick() {
        _events.trySend(SalonEvent.NuevaMesa)
    }

    fun onEditarMesaClick(mesa: Mesa) {
        _events.trySend(SalonEvent.EditarMesa(mesa))
    }

    fun onSolicitarBajaMesa(mesa: Mesa) {
        _uiState.update { it.copy(mesaADarDeBaja = mesa) }
    }

    fun onCancelarBajaMesa() {
        _uiState.update { it.copy(mesaADarDeBaja = null) }
    }

    fun onConfirmarBajaMesa() {
        val mesaAEliminar = _uiState.value.mesaADarDeBaja ?: return

        _uiState.update { state ->
            val listaSinMesa = state.mesas.filterNot { it.id == mesaAEliminar.id }
            state.copy(
                mesas = listaSinMesa,
                mesaADarDeBaja = null
            )
        }
    }
}
