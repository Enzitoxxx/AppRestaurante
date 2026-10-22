package com.restaurant.app.ui.features.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.ui.features.login.components.LoginFooter
import com.restaurant.app.ui.features.login.components.LoginForm
import com.restaurant.app.ui.features.login.components.LoginHeader
import com.restaurant.app.ui.theme.AppTheme

/**
 * PANTALLA DE INICIO DE SESIÓN (Stateful Composable).
 *
 * Funcionalidad principal:
 * - Sirve como punto de entrada de la pantalla de Login conectada con la lógica de negocio ([LoginViewModel]).
 * - Observa el estado de la UI ([LoginUiState]) de forma segura respetando el ciclo de vida.
 * - Escucha eventos únicos o de una sola vez ([LoginEvent]), como la navegación exitosa al completar el inicio de sesión.
 * - Delega la renderización visual a [LoginContent] siguiendo el patrón de diseño Stateful/Stateless.
 *
 * @param onLoginSuccess Callback que se ejecuta cuando el inicio de sesión es exitoso (ej. navegar a la pantalla principal/Home).
 * @param onForgotPasswordClick Callback para navegar a la pantalla de recuperación de contraseña.
 * @param onRegisterClick Callback para navegar a la pantalla de registro de nuevo usuario.
 * @param viewModel Instancia del [LoginViewModel] encargada de gestionar el estado y las acciones del login.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    // Suscripción al estado de la interfaz de usuario observando cambios de forma segura ante el ciclo de vida
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Manejo de eventos de una sola vez (One-shot events) emitidos por el ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.LoginSuccess -> onLoginSuccess()
            }
        }
    }

    // Renderizado de la UI pura pasando el estado actual y los callbacks de interacción
    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onLoginClick = viewModel::onLoginClick,
        onForgotPasswordClick = onForgotPasswordClick,
        onRegisterClick = onRegisterClick
    )
}

/**
 * CONTENIDO VISUAL DE LA PANTALLA DE LOGIN (Stateless Composable).
 *
 * Funcionalidad principal:
 * - Define únicamente la estructura gráfica de la pantalla a partir de un estado ([LoginUiState]).
 * - No depende de [LoginViewModel], lo que facilita las pruebas unitarias de la interfaz y la previsualización en Android Studio.
 * - Organiza los elementos visuales en tres componentes principales:
 *     1. [LoginHeader]: Encabezado con logo/título del restaurante.
 *     2. [LoginForm]: Formulario con campos de texto para email, contraseña y botón de ingreso.
 *     3. [LoginFooter]: Enlace o botón para dirigir al flujo de registro de usuario.
 * - Administra adaptabilidad, insets del sistema (barra de estado, navegación y teclado virtual) y scroll cuando el contenido supera el alto de pantalla.
 *
 * @param state Estado actual de la UI con los datos ingresados y posibles errores de validación.
 * @param onEmailChange Callback invocado cuando el usuario escribe en el campo de correo electrónico.
 * @param onPasswordChange Callback invocado cuando el usuario escribe en el campo de contraseña.
 * @param onTogglePasswordVisibility Callback para alternar entre mostrar u ocultar la contraseña.
 * @param onLoginClick Callback invocado al presionar el botón para iniciar sesión.
 * @param onForgotPasswordClick Callback para el enlace "¿Olvidaste tu contraseña?".
 * @param onRegisterClick Callback para el botón/enlace de registro de cuenta.
 * @param modifier Modificador de Compose para personalizar estilos o dimensiones desde fuera.
 */
@Composable
fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // BoxWithConstraints permite conocer el alto máximo disponible en pantalla para un scroll adaptativo
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()      // Espaciado para evitar superposición con la barra de estado superior
            .navigationBarsPadding()  // Espaciado para evitar superposición con los botones de navegación inferiores
            .imePadding()             // Ajusta la pantalla cuando se despliega el teclado virtual (IME)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = maxHeight)   // Ocupa al menos el alto total para centrar contenido, permitiendo scroll si es necesario
                .verticalScroll(rememberScrollState()) // Habilita desplazamiento vertical si el teclado o pantallas pequeñas lo requieren
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Encabezado visual (logo, mensaje de bienvenida)
            LoginHeader()

            Spacer(Modifier.height(28.dp))

            // Formulario principal de ingreso (campos email, password, error, ingresar)
            LoginForm(
                state = state,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                onForgotPasswordClick = onForgotPasswordClick,
                onLoginClick = onLoginClick
            )

            Spacer(Modifier.height(20.dp))

            // Pie de página (invitación a registrarse)
            LoginFooter(
                onRegisterClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * PREVISUALIZACIÓN DE LA PANTALLA EN TIEMPO DE DISEÑO.
 *
 * Permite visualizar el comportamiento gráfico del componente [LoginContent] en la pestaña "Design"
 * de Android Studio usando datos falsos/iniciales sin necesidad de ejecutar la app en un dispositivo o emulador.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginContent(
            state = LoginUiState(email = "lucia@labuenamesa.com.ar", password = "12345678"),
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onForgotPasswordClick = {},
            onRegisterClick = {}
        )
    }
}

