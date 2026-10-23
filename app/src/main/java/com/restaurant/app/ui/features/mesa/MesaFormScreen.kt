package com.restaurant.app.ui.features.mesa

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
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppTextField
import com.restaurant.app.core.designsystem.PrimaryButton
import com.restaurant.app.ui.features.mesa.components.CapacidadStepperCard
import com.restaurant.app.ui.features.mesa.components.MesaFormHeader
import com.restaurant.app.ui.features.mesa.components.ZonaRestauranteCards
import com.restaurant.app.ui.theme.AppTheme

/*
 * PANTALLA CONFIGURACIÓN DE MESA (Stateful Composable).
 * Conectada al MesaFormViewModel: gestiona el estado y escucha eventos de guardado/retorno.
 */
@Composable
fun MesaFormScreen(
    onVolverClick: () -> Unit,
    onMesaGuardada: () -> Unit,
    viewModel: MesaFormViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                MesaFormEvent.MesaGuardada -> onMesaGuardada()
                MesaFormEvent.Volver -> onVolverClick()
            }
        }
    }

    MesaFormContent(
        state = state,
        onNumeroChange = viewModel::onNumeroChange,
        onIncrementarCapacidad = viewModel::onIncrementarCapacidad,
        onDecrementarCapacidad = viewModel::onDecrementarCapacidad,
        onZonaChange = viewModel::onZonaChange,
        onVolverClick = viewModel::onVolverClick,
        onGuardarClick = viewModel::onGuardarClick
    )
}

/*
  CONTENIDO VISUAL DEL FORMULARIO DE MESA
  Sin ViewModel: fácil de probar e inspeccionar en la vista previa.
 */
@Composable
fun MesaFormContent(
    state: MesaFormUiState,
    onNumeroChange: (String) -> Unit,
    onIncrementarCapacidad: () -> Unit,
    onDecrementarCapacidad: () -> Unit,
    onZonaChange: (String) -> Unit,
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
            // 1. Encabezado
            MesaFormHeader(onVolverClick = onVolverClick)

            Spacer(Modifier.height(20.dp))

            // 2. Campo numero o codigo de mesa (Reutilizando AppTextField)
            AppTextField(
                label = stringResource(R.string.mesa_form_label_numero),
                value = state.numeroOMesaCodigo,
                onValueChange = onNumeroChange,
                errorText = state.errorNumero?.let { stringResource(it) },
                enabled = !state.estaCargando
            )

            Spacer(Modifier.height(16.dp))

            // 3. Tarjeta de Capacidad con Stepper (- y +)
            CapacidadStepperCard(
                capacidadPersonas = state.capacidadPersonas,
                onIncrementar = onIncrementarCapacidad,
                onDecrementar = onDecrementarCapacidad
            )

            Spacer(Modifier.height(16.dp))

            // 4. Tarjetas de seleccion de Zona (Salón / Patio)
            ZonaRestauranteCards(
                zonaSeleccionada = state.zonaSeleccionada,
                onZonaChange = onZonaChange
            )

            Spacer(Modifier.height(24.dp))

            // 5. Botón Principal "Guardar mesa" (Reutilizando PrimaryButton)
            PrimaryButton(
                text = stringResource(R.string.mesa_form_boton_guardar),
                onClick = onGuardarClick,
                isLoading = state.estaCargando,
                trailingIcon = Icons.Outlined.Save
            )
        }
    }
}

/* Previsualización en tiempo de diseño en Android Studio. */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MesaFormScreenPreview() {
    AppTheme {
        MesaFormContent(
            state = MesaFormUiState(
                numeroOMesaCodigo = "Mesa 07",
                capacidadPersonas = 4,
                zonaSeleccionada = "Salón"
            ),
            onNumeroChange = {},
            onIncrementarCapacidad = {},
            onDecrementarCapacidad = {},
            onZonaChange = {},
            onVolverClick = {},
            onGuardarClick = {}
        )
    }
}