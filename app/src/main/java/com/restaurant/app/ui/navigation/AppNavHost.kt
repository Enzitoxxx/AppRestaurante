package com.restaurant.app.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.restaurant.app.ui.features.carta.CartaScreen
import com.restaurant.app.ui.features.forgotpassword.ForgotPasswordScreen
import com.restaurant.app.ui.features.login.LoginScreen
import com.restaurant.app.ui.features.producto.ProductoScreen
import com.restaurant.app.ui.features.registro.RegistroScreen
import com.restaurant.app.ui.features.splash.SplashScreen

@Composable
fun AppNavHost() {
    val backStack = rememberNavBackStack(Screen.Splash)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Screen.Splash> {
                SplashScreen(
                    onFinished = {
                        backStack.add(Screen.Login)
                        backStack.remove(Screen.Splash)
                    }
                )
            }
            entry<Screen.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.clear()               // no se vuelve al Login con "atrás"
                        backStack.add(Screen.Home)
                    },
                    onRegisterClick = { backStack.add(Screen.Registro) }, // el .add apila la nueva pantalla encima de la actual.
                    onForgotPasswordClick = { backStack.add(Screen.ForgotPassword) }
                )
            }

            entry<Screen.Home> {
                Text("Home (pendiente)")   // provisorio, para armar la pantalla del administrador
                /*
                CartaScreen(
                    onAgregarProductoClick = {
                        backStack.add(Screen.ProductoForm()) // Abre Alta de producto
                    },
                    onEditarProductoClick = { producto ->
                        backStack.add(Screen.ProductoForm(productoId = producto.id)) // Abre Edición de producto
                    }
                )
                */
            }

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

            entry<Screen.ForgotPassword> {
                ForgotPasswordScreen(
                    onBackClick = { backStack.removeLastOrNull() },
                    onEmailSent = { backStack.removeLastOrNull() }
                )
            }

            //Pantalla de carta
            entry<Screen.Carta> {
                CartaScreen(
                    onAgregarProductoClick = {
                        backStack.add(Screen.ProductoForm()) //Pasa/ navega al modo Alta
                    },
                    onEditarProductoClick = {producto -> backStack.add(Screen.ProductoForm(productoId = producto.id))} //Pasa/navega al modo Edicion
                )
            }

            //Pantalla de Alta / Edicion
            entry<Screen.ProductoForm> { key ->
                ProductoScreen(
                    productoId = key.productoId,
                    onVolverClick = { backStack.removeLastOrNull() },
                    onProductoGuardado = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}