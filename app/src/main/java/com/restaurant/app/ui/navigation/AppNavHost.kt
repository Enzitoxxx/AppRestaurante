package com.restaurant.app.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.restaurant.app.core.designsystem.NavegacionItem
import com.restaurant.app.ui.features.carta.CartaScreen
import com.restaurant.app.ui.features.forgotpassword.ForgotPasswordScreen
import com.restaurant.app.ui.features.login.LoginScreen
import com.restaurant.app.ui.features.mesa.MesaFormScreen
import com.restaurant.app.ui.features.producto.ProductoScreen
import com.restaurant.app.ui.features.registro.RegistroScreen
import com.restaurant.app.ui.features.salon.SalonScreen
import com.restaurant.app.ui.features.splash.SplashScreen

/*
   NAVIGATION 3 y BACKSTACK :

   1. Navigation 3 (Jetpack Navigation 3 - Type-Safe Navigation):
      Es la librería oficial más reciente de Google para manejar navegación en Jetpack Compose.
      A diferencia de las versiones viejas que usaban textos ("routes") propensos a errores,
      Navigation 3 usa objetos tipados de Kotlin (Screen.Login, Screen.Carta, Screen.ProductoForm).
      Si cambias el nombre o propiedad de una pantalla, el compilador de Kotlin te avisa inmediatamente antes de ejecutar la app.

   2. backStack (Pila de Navegación / Historial):
      funciona exactamente como una pila de cartas (LIFO: Last In, First Out) que guarda la historia de pantallas que el usuario tiene abiertas:
      - backStack.add(Screen.X): Agrega/Apila una nueva pantalla arriba.
      - backStack.removeLastOrNull(): Quita la pantalla actual para volver a la anterior.
      - backStack.clear(): Vacía todo el historial (ej: al hacer Login para no regresar atrás).
*/

@Composable
fun AppNavHost() {
    // Inicializa la pila de navegación comenzando en la pantalla Splash
    val backStack = rememberNavBackStack(Screen.Splash)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            // Pantalla Splash
            entry<Screen.Splash> {
                SplashScreen(
                    onFinished = {
                        backStack.add(Screen.Login)
                        backStack.remove(Screen.Splash)
                    }
                )
            }

            // Pantalla Login
            entry<Screen.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.clear()               // No se vuelve al Login con "atrás"
                        backStack.add(Screen.Home)
                    },
                    onRegisterClick = { backStack.add(Screen.Registro) },
                    onForgotPasswordClick = { backStack.add(Screen.ForgotPassword) }
                )
            }

            // Pantalla Home (TEMPORAL PARA PRUEBAS EN DISPOSITIVO MOSTRANDO EL SALÓN Y SUS MESAS)
            entry<Screen.Home> {
                SalonScreen(
                    onMesaClick = { mesa ->
                        backStack.add(Screen.MesaForm(mesaId = mesa.id))
                    },
                    onEditarMesaClick = { mesa ->
                        backStack.add(Screen.MesaForm(mesaId = mesa.id))
                    },
                    onNuevaMesaClick = {
                        backStack.add(Screen.MesaForm())
                    },
                    onNavegarA = { item ->
                        when (item) {
                            NavegacionItem.Carta -> backStack.add(Screen.Carta)
                            NavegacionItem.Salon -> { /* Ya estamos aquí */ }
                            NavegacionItem.Inicio -> { /* Inicio */ }
                            else -> { /* Otros */ }
                        }
                    }
                )
            }

            // Pantalla Registro de Usuario
            entry<Screen.Registro> {
                RegistroScreen(
                    onRegistroSuccess = {
                        backStack.clear()
                        backStack.add(Screen.Home)
                    },
                    onLoginClick = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            // Pantalla Recuperar Contraseña
            entry<Screen.ForgotPassword> {
                ForgotPasswordScreen(
                    onBackClick = { backStack.removeLastOrNull() },
                    onEmailSent = { backStack.removeLastOrNull() }
                )
            }

            // Pantalla Carta & Menú
            entry<Screen.Carta> {
                CartaScreen(
                    onAgregarProductoClick = {
                        backStack.add(Screen.ProductoForm()) // Navega a Alta
                    },
                    onEditarProductoClick = { producto ->
                        backStack.add(Screen.ProductoForm(productoId = producto.id)) // Navega a Edición
                    },
                    onNavegarA = { item ->
                        when (item) {
                            NavegacionItem.Salon -> backStack.add(Screen.Salon)
                            NavegacionItem.Carta -> { /* Ya estamos aquí */ }
                            NavegacionItem.Inicio -> backStack.add(Screen.Home)
                            else -> { /* Otros ítems */ }
                        }
                    }
                )
            }

            // Pantalla Alta / Edición de Producto
            entry<Screen.ProductoForm> { key ->
                ProductoScreen(
                    productoId = key.productoId,
                    onVolverClick = { backStack.removeLastOrNull() },
                    onProductoGuardado = { backStack.removeLastOrNull() }
                )
            }

            // Pantalla Mapa del Salón
            entry<Screen.Salon> {
                SalonScreen(
                    onMesaClick = { mesa ->
                        // TODO: Navegar a la comanda/detalle de la mesa
                    },
                    onEditarMesaClick = { mesa ->
                        backStack.add(Screen.MesaForm(mesaId = mesa.id)) // Editar mesa
                    },
                    onNuevaMesaClick = {
                        backStack.add(Screen.MesaForm()) // Alta mesa
                    },
                    onNavegarA = { item ->
                        when (item) {
                            NavegacionItem.Carta -> backStack.add(Screen.Carta)
                            NavegacionItem.Salon -> { /* Ya estamos aquí */ }
                            NavegacionItem.Inicio -> backStack.add(Screen.Home)
                            else -> { /* Otros ítems */ }
                        }
                    }
                )
            }

            // Pantalla Configuración de Mesa
            entry<Screen.MesaForm> { key ->
                MesaFormScreen(
                    onVolverClick = { backStack.removeLastOrNull() },
                    onMesaGuardada = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
