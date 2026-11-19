package com.restaurant.app.ui.features.mas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
 * VIEWMODEL DEL MENÚ MÁS (MasViewModel).
 *
 * Maneja las opciones del menú secundario, incluyendo el cierre de sesión del usuario actual.
 * Al confirmar el cierre de sesión, invoca a `authRepository.cerrarSesion()` para limpiar el ID en
 * SharedPreferences y emite el evento `MasEvent.SesionCerrada` para retornar al Login.
 */
class MasViewModel(
    private val authRepository: AuthRepository = Repositorios.auth
) : ViewModel() {

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
            authRepository.cerrarSesion()
            _events.send(MasEvent.SesionCerrada)
        }
    }
}
