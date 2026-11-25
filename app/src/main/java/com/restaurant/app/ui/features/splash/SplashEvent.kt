package com.restaurant.app.ui.features.splash

/*
 * EVENTOS DE NAVEGACIÓN DE LA PANTALLA SPLASH (SplashEvent).
 *
 * Determina el destino final al iniciar la app según el estado de la sesión:
 * - IrAHome: Si hay un usuario autenticado activo guardado.
 * - IrALogin: Si no hay usuario en sesión o la cuenta se desactivó/eliminó.
 */
sealed interface SplashEvent {
    data object IrAHome : SplashEvent
    data object IrALogin : SplashEvent
}
