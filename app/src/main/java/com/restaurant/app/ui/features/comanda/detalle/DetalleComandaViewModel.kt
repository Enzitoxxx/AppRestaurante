package com.restaurant.app.ui.features.comanda.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.domain.model.ComandaItemModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleComandaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        DetalleComandaUiState(items = getMockItems())
    )
    val uiState: StateFlow<DetalleComandaUiState> = _uiState.asStateFlow()

    private val _events = Channel<DetalleComandaEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onCobrarClick() {
        viewModelScope.launch {
            _events.send(DetalleComandaEvent.ComandaCobrada)
        }
    }

    fun onAbrirAnularSheet() {
        _uiState.update { it.copy(mostrarAnularSheet = true) }
    }

    fun onCerrarAnularSheet() {
        _uiState.update { it.copy(mostrarAnularSheet = false) }
    }

    fun onMotivoSeleccionado(motivo: String) {
        _uiState.update { it.copy(motivoAnulacionSeleccionado = motivo) }
    }

    fun onConfirmarAnulacion() {
        viewModelScope.launch {
            _uiState.update { it.copy(mostrarAnularSheet = false) }
            _events.send(DetalleComandaEvent.ComandaAnulada)
        }
    }

    private companion object {
        fun getMockItems() = listOf(
            ComandaItemModel("1", 2, "Empanadas de carne", 2200.0, "Con limón extra"),
            ComandaItemModel("2", 1, "Bife de chorizo con papas", 14800.0, "Punto medio"),
            ComandaItemModel("3", 2, "Cerveza Tirada", 3500.0),
            ComandaItemModel("4", 1, "Flan casero", 2400.0)
        )
    }
}