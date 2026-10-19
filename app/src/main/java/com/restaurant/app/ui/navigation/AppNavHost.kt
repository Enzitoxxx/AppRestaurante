package com.restaurant.app.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.restaurant.app.ui.features.login.LoginScreen
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
                    onForgotPasswordClick = { /* TODO: Screen.ForgotPassword */ },
                    onRegisterClick = { /* TODO: Screen.Register */ }
                )
            }
            entry<Screen.Home> {
                Text("Home (pendiente)")   // provisorio
            }
        }
    )
}