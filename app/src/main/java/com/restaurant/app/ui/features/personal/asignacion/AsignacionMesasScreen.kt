package com.restaurant.app.ui.features.personal.asignacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.PrimaryButton
import com.restaurant.app.domain.model.EstadoMesa
import com.restaurant.app.domain.model.Mesa
import com.restaurant.app.ui.features.personal.asignacion.components.AsignacionHeader
import com.restaurant.app.ui.features.personal.asignacion.components.MesaSeleccionableCard
import com.restaurant.app.ui.theme.AppTheme

/*
 * PANTALLA ASIGNACIÓN DE MESAS (Stateful Composable).
 *
 * Conectada al AsignacionMesasViewModel:
 * - Observa la lista de mesas y las seleccionadas.
 * - Escucha eventos de guardado o retorno.
 */
@Composable
fun AsignacionMesasScreen(
    empleadoId: String,
    onVolverClick: () -> Unit = {},
    onAsignacionGuardada: () -> Unit = {},
    viewModel: AsignacionMesasViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Carga el empleado y sus mesas al abrir la pantalla
    LaunchedEffect(empleadoId) {
        viewModel.cargarEmpleado(empleadoId)
    }

    // Escucha eventos únicos de una sola vez
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AsignacionMesasEvent.AsignacionGuardada -> onAsignacionGuardada()
                AsignacionMesasEvent.Volver -> onVolverClick()
            }
        }
    }

    AsignacionMesasContent(
        state = state,
        onToggleMesaSeleccionada = viewModel::onToggleMesaSeleccionada,
        onVolverClick = viewModel::onVolverClick,
        onGuardarAsignacionClick = viewModel::onGuardarAsignacionClick
    )
}

/**
 * CONTENIDO VISUAL DE ASIGNACIÓN DE MESAS (Stateless Composable).
 *
 * Sin ViewModel: dibuja el formulario dinámico a partir del estado.
 */
@Composable
fun AsignacionMesasContent(
    state: AsignacionMesasUiState,
    onToggleMesaSeleccionada: (String) -> Unit,
    onVolverClick: () -> Unit,
    onGuardarAsignacionClick: () -> Unit,
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
            // 1. Encabezado "← Asignación de Mesas" + Subtítulo
            AsignacionHeader(
                nombreEmpleado = state.nombreEmpleado,
                rolEmpleado = state.rolEmpleado,
                onVolverClick = onVolverClick
            )

            Spacer(Modifier.height(20.dp))

            // 2. Etiqueta de Sección: "SELECCIONAR MESAS A CARGO"
            Text(
                text = stringResource(R.string.asignacion_seccion_label),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            // 3. Lista de Mesas Seleccionables con Checkbox
            state.mesasVisibles.forEach { mesa ->
                MesaSeleccionableCard(
                    mesa = mesa,
                    estaSeleccionada = state.idsMesasSeleccionadas.contains(mesa.id),
                    onToggleSeleccion = onToggleMesaSeleccionada,
                    asignadaA = state.asignadaAOtro(mesa)
                )
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(16.dp))

            // 4. Botón Principal con texto dinámico según cantidad seleccionada
            val textoBoton = when (state.totalSeleccionadas) {
                0 -> stringResource(R.string.asignacion_boton_confirmar_cero)
                1 -> stringResource(R.string.asignacion_boton_confirmar_uno)
                else -> stringResource(R.string.asignacion_boton_confirmar_varias, state.totalSeleccionadas)
            }

            PrimaryButton(
                text = textoBoton,
                onClick = onGuardarAsignacionClick,
                isLoading = state.estaCargando,
                trailingIcon = Icons.Outlined.Check
            )
        }
    }
}

/** Previsualización en tiempo de diseño en Android Studio. */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AsignacionMesasScreenPreview() {
    AppTheme {
        AsignacionMesasContent(
            state = AsignacionMesasUiState(
                nombreEmpleado = "Camila Gómez",
                rolEmpleado = "Camarera",
                mesasDisponibles = listOf(
                    Mesa("1", 1, "Salon", 4, EstadoMesa.LIBRE),
                    Mesa("2", 2, "Salon", 4, EstadoMesa.OCUPADA),
                    Mesa("3", 3, "Salon", 6, EstadoMesa.OCUPADA),
                    Mesa("4", 4, "Salon", 2, EstadoMesa.CUENTA),
                    Mesa("5", 5, "Patio", 2, EstadoMesa.LIBRE)
                ),
                idsMesasSeleccionadas = setOf("1", "2", "3")
            ),
            onToggleMesaSeleccionada = {},
            onVolverClick = {},
            onGuardarAsignacionClick = {}
        )
    }
}
