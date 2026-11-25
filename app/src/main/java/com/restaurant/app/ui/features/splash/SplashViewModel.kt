package com.restaurant.app.ui.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.app.data.Repositorios
import com.restaurant.app.domain.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/*
 * VIEWMODEL DE LA PANTALLA SPLASH (SplashViewModel).
 *
 * Muestra la pantalla inicial con la versión de la app durante 1500 ms y verifica si existe una sesión
 * activa consultando `authRepository.usuarioActual.first()`.
 *
 * ¿Por qué usamos `first()`?
 * `first()` lee únicamente la primera emisión del Flow en el instante que expira el temporizador
 * y cancela la suscripción inmediatamente, evitando quedar escuchando cambios futuros del estado
 * mientras se navega hacia la siguiente pantalla.
 *
 * Si hay un ID guardado en SharedPreferences pero el usuario fue eliminado de Room o está inactivo
 * (estaActivo = false), `usuarioActual` emitirá null y la app redirigirá automáticamente a Login.
 */
class SplashViewModel(
    private val authRepository: AuthRepository = Repositorios.auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState(appVersion = "1.0"))
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _events = Channel<SplashEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(1500)
            // Consultamos el usuario actual de forma puntual usando first()
            val usuario = authRepository.usuarioActual.first()
            if (usuario != null) {
                _events.send(SplashEvent.IrAHome)
            } else {
                _events.send(SplashEvent.IrALogin)
            }
        }
    }
}
