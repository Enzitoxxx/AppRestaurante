package com.restaurant.app.ui.features.registro

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
import com.restaurant.app.ui.features.registro.components.RegistroFooter
import com.restaurant.app.ui.features.registro.components.RegistroForm
import com.restaurant.app.ui.features.registro.components.RegistroHeader
import com.restaurant.app.ui.theme.AppTheme

/*
  PANTALLA CON ESTADO (Stateful Composable):
  - Se conecta con el RegistroViewModel.
  - Escucha el estado (uiState) y los eventos de navegación (events).
  - Pasa las funciones y datos a RegistroContent para dibujar la interfaz.
*/
@Composable
fun RegistroScreen(
    onRegistroSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    // Escuchamos el estado del ViewModel de forma segura respetando el ciclo de vida de la app
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Escuchamos eventos de una sola vez (Channel/Flow) emitidos por el ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                // Cuando el ViewModel avisa que el registro fue exitoso, ejecutamos la navegación a Home
                RegistroEvent.RegistroSuccess -> onRegistroSuccess()
            }
        }
    }

    // Le pasamos el estado y los callbacks al componente visual sin estado (Stateless)
    RegistroContent(
        state = state,
        onNombreChange = viewModel::onNombreChange,
        onCorreoChange = viewModel::onCorreoChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        onRestauranteChange = viewModel::onRestauranteChange,
        onContrasenaChange = viewModel::onContrasenaChange,
        onToggleContrasenaVisibility = viewModel::onToggleContrasenaVisibility,
        onRegistroClick = viewModel::onRegistroClick,
        onLoginClick = onLoginClick
    )
}

/*
  CONTENIDO VISUAL SIN ESTADO (Stateless Composable):
  - No depende directamente del ViewModel (fácil de probar y previsualizar).
  - Recibe solo el estado (state) y las funciones lambda.
  - Se encarga de organizar los 3 subcomponentes: Header, Form y Footer.
*/
@Composable
fun RegistroContent(
    state: RegistroUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onRestauranteChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onToggleContrasenaVisibility: () -> Unit,
    onRegistroClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // BoxWithConstraints: nos da las dimensiones máximas disponibles en la pantalla
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()      // Evita que el contenido tape la barra de estado superior (batería, hora)
            .navigationBarsPadding()  // Evita que el contenido tape los botones de navegación inferiores
            .imePadding()             // Empuja el contenido hacia arriba cuando se abre el teclado virtual
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = maxHeight)             // Ocupa como mínimo el alto total para centrar el contenido
                .verticalScroll(rememberScrollState()) // Permite hacer scroll vertical si el teclado tape algún campo
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Encabezado con título y subtítulo
            RegistroHeader()

            Spacer(Modifier.height(28.dp))

            // 2. Formulario con los 5 campos de texto y el botón "Crear cuenta"
            RegistroForm(
                state = state,
                onNombreChange = onNombreChange,
                onCorreoChange = onCorreoChange,
                onTelefonoChange = onTelefonoChange,
                onRestauranteChange = onRestauranteChange,
                onContrasenaChange = onContrasenaChange,
                onToggleContrasenaVisibility = onToggleContrasenaVisibility,
                onRegistroClick = onRegistroClick
            )

            Spacer(Modifier.height(20.dp))

            // 3. Pie de página con opción "¿Ya tenés cuenta? Ingresar"
            RegistroFooter(
                onLoginClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/*
  PREVISUALIZACIÓN EN TIEMPO DE DISEÑO:
  - Muestra cómo se ve la pantalla sin necesidad de ejecutar la app.
*/
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegistroScreenPreview() {
    AppTheme {
        RegistroContent(
            state = RegistroUiState(
                nombre = "Timoteo Wierna",
                correo = "timopa@labuenamesa.com.ar",
                telefono = "+54 9 385 4567-8900",
                restaurante = "La Buena Mesa"
            ),
            onNombreChange = {},
            onCorreoChange = {},
            onTelefonoChange = {},
            onRestauranteChange = {},
            onContrasenaChange = {},
            onToggleContrasenaVisibility = {},
            onRegistroClick = {},
            onLoginClick = {}
        )
    }
}
