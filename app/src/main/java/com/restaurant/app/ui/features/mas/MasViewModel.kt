package com.restaurant.app.ui.features.mas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MasUiState())
    val uiState: StateFlow<MasUiState> = _uiState.asStateFlow()

    private val _events = Channel<MasEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onSolicitarCerrarSesion() {
        _uiState.update { it.copy(mostrarDialogoCerrarSesion = true) }
    }

    fun onCancelarCerrarSesion() {
        _uiState.update { it.copy(mostrarDialogoCerrarSesion = false) }
    }

    fun onConfirmarCerrarSesion() {
        _uiState.update { it.copy(mostrarDialogoCerrarSesion = false) }
        viewModelScope.launch {
            // TODO: llamar a AuthRepository.cerrarSesion() cuando exista
            _events.send(MasEvent.SesionCerrada)
        }
    }
}
