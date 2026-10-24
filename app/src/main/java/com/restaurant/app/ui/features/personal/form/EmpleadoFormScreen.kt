package com.restaurant.app.ui.features.personal.form

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.PrimaryButton
import com.restaurant.app.ui.features.personal.form.components.CredencialesCard
import com.restaurant.app.ui.features.personal.form.components.EmpleadoFormFields
import com.restaurant.app.ui.features.personal.form.components.EmpleadoFormHeader
import com.restaurant.app.ui.theme.AppTheme

/**
 * PANTALLA ALTA / EDICIÓN DE EMPLEADO (Stateful Composable).
 * Conectada al EmpleadoFormViewModel: gestiona el formulario y escucha eventos de guardado o retorno.
 */
@Composable
fun EmpleadoFormScreen(
    empleadoId: String? = null,
    onVolverClick: () -> Unit,
    onEmpleadoGuardado: () -> Unit,
    viewModel: EmpleadoFormViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(empleadoId) {
        if (empleadoId != null) {
            viewModel.cargarEmpleadoParaEdicion(
                nombre = "Camila Gómez",
                telefono = "+54 9 11 2345-6789",
                correo = "camila@labuenamesa.com.ar",
                rol = "Camarero / Mozo",
                turno = "Turno Noche",
                asignacion = "Salón Principal"
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                EmpleadoFormEvent.EmpleadoGuardado -> onEmpleadoGuardado()
                EmpleadoFormEvent.Volver -> onVolverClick()
            }
        }
    }

    EmpleadoFormContent(
        state = state,
        onNombreChange = viewModel::onNombreChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        onCorreoChange = viewModel::onCorreoChange,
        onRolChange = viewModel::onRolChange,
        onTurnoChange = viewModel::onTurnoChange,
        onAsignacionChange = viewModel::onAsignacionChange,
        onVolverClick = viewModel::onVolverClick,
        onGuardarClick = viewModel::onGuardarClick
    )
}

/**
 * CONTENIDO VISUAL DEL FORMULARIO DE EMPLEADO (Stateless Composable).
 * Sin ViewModel: fácil de probar e inspeccionar en la vista previa.
 */
@Composable
fun EmpleadoFormContent(
    state: EmpleadoFormUiState,
    onNombreChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onTurnoChange: (String) -> Unit = {},
    onAsignacionChange: (String) -> Unit = {},
    onVolverClick: () -> Unit,
    onGuardarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = maxHeight)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // 1. Encabezado "← Registrar Empleado"
            EmpleadoFormHeader(
                esEdicion = state.esEdicion,
                onVolverClick = onVolverClick
            )

            Spacer(Modifier.height(20.dp))

            // 2. Campos del Formulario (Nombre, Teléfono, Correo, Rol, Turno, Asignación)
            EmpleadoFormFields(
                state = state,
                onNombreChange = onNombreChange,
                onTelefonoChange = onTelefonoChange,
                onCorreoChange = onCorreoChange,
                onRolChange = onRolChange,
                onTurnoChange = onTurnoChange,
                onAsignacionChange = onAsignacionChange
            )

            Spacer(Modifier.height(20.dp))

            // 3. Botón Principal "Registrar empleado" (Reutilizando PrimaryButton)
            PrimaryButton(
                text = stringResource(R.string.empleado_form_boton_guardar),
                onClick = onGuardarClick,
                isLoading = state.estaCargando,
                trailingIcon = Icons.Outlined.Badge
            )

            Spacer(Modifier.height(20.dp))

            // 4. Tarjeta Verde con Credenciales Temporales Generadas
            CredencialesCard(
                usuario = state.usuarioGenerado,
                claveTemporal = state.claveTemporalGenerada
            )
        }
    }
}

/** Previsualización en tiempo de diseño en Android Studio. */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmpleadoFormScreenPreview() {
    AppTheme {
        EmpleadoFormContent(
            state = EmpleadoFormUiState(
                nombre = "Camila Gómez",
                telefono = "+54 9 11 2345-6789",
                correo = "camila@labuenamesa.com.ar",
                rol = "Camarero / Mozo",
                turno = "Turno Noche",
                asignacion = "Salón Principal",
                usuarioGenerado = "camila.gomez",
                claveTemporalGenerada = "Mesa2026!"
            ),
            onNombreChange = {},
            onTelefonoChange = {},
            onCorreoChange = {},
            onRolChange = {},
            onTurnoChange = {},
            onAsignacionChange = {},
            onVolverClick = {},
            onGuardarClick = {}
        )
    }
}
