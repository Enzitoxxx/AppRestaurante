package com.restaurant.app.ui.features.personal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.core.designsystem.AppBottomBar
import com.restaurant.app.core.designsystem.NavegacionItem
import com.restaurant.app.domain.model.Empleado
import com.restaurant.app.ui.features.personal.components.EmpleadoCard
import com.restaurant.app.ui.features.personal.components.PersonalHeader
import com.restaurant.app.ui.theme.AppTheme

/**
 * PANTALLA EQUIPO DE TRABAJO / PERSONAL (Stateful Composable).
 * Conectada al PersonalViewModel: observa el estado de los empleados y escucha eventos de acción.
 */
@Composable
fun PersonalScreen(
    onAgregarEmpleadoClick: () -> Unit = {},
    onEditarEmpleadoClick: (Empleado) -> Unit = {},
    onAsignarMesasClick: (Empleado) -> Unit = {},
    onNavegarA: (NavegacionItem) -> Unit = {},
    viewModel: PersonalViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PersonalEvent.EditarEmpleado -> onEditarEmpleadoClick(event.empleado)
                is PersonalEvent.AsignarMesasEmpleado -> onAsignarMesasClick(event.empleado)
                PersonalEvent.AgregarEmpleado -> onAgregarEmpleadoClick()
            }
        }
    }

    PersonalContent(
        state = state,
        onAgregarEmpleadoClick = viewModel::onAgregarEmpleadoClick,
        onEditarEmpleadoClick = viewModel::onEditarEmpleadoClick,
        onAsignarMesasClick = viewModel::onAsignarMesasClick,
        onSolicitarBajaEmpleado = viewModel::onSolicitarBajaEmpleado,
        onCancelarBajaEmpleado = viewModel::onCancelarBajaEmpleado,
        onConfirmarBajaEmpleado = viewModel::onConfirmarBajaEmpleado,
        onNavegarA = onNavegarA
    )
}

/**
 * CONTENIDO VISUAL DE PERSONAL (Stateless Composable).
 * Sin ViewModel: fácil de probar e inspeccionar en la vista previa.
 */
@Composable
fun PersonalContent(
    state: PersonalUiState,
    onAgregarEmpleadoClick: () -> Unit,
    onEditarEmpleadoClick: (Empleado) -> Unit = {},
    onAsignarMesasClick: (Empleado) -> Unit = {},
    onSolicitarBajaEmpleado: (Empleado) -> Unit = {},
    onCancelarBajaEmpleado: () -> Unit = {},
    onConfirmarBajaEmpleado: () -> Unit = {},
    onNavegarA: (NavegacionItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            AppBottomBar(
                rutaActual = "personal",
                onItemClick = { item -> onNavegarA(item) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // 1. Encabezado "Equipo de trabajo" + Resumen + Botón '+'
                PersonalHeader(
                    totalEmpleados = state.totalEmpleados,
                    onAgregarEmpleadoClick = onAgregarEmpleadoClick
                )

                Spacer(Modifier.height(20.dp))

                // 2. Lista LazyColumn de Empleados Registrados
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = state.empleados,
                        key = { empleado -> empleado.id }
                    ) { empleado ->
                        EmpleadoCard(
                            empleado = empleado,
                            onEditarClick = onEditarEmpleadoClick,
                            onAsignarMesasClick = onAsignarMesasClick,
                            onBajaClick = onSolicitarBajaEmpleado
                        )
                    }
                }
            }

            // Diálogo Emergente (AlertDialog) de Confirmación para Baja/Eliminación de Empleado
            state.empleadoADarDeBaja?.let { empleado ->
                AlertDialog(
                    onDismissRequest = onCancelarBajaEmpleado,
                    title = {
                        Text(
                            text = "¿Dar de baja empleado?",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    text = {
                        Text(
                            text = "¿Estás seguro de dar de baja a \"${empleado.nombre}\"? Dejará de aparecer en la lista del equipo de trabajo.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = onConfirmarBajaEmpleado) {
                            Text(
                                text = "Dar de baja",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = onCancelarBajaEmpleado) {
                            Text(text = "Cancelar")
                        }
                    }
                )
            }
        }
    }
}

/** Previsualización en tiempo de diseño dentro de Android Studio. */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PersonalScreenPreview() {
    AppTheme {
        PersonalContent(
            state = PersonalUiState(
                empleados = listOf(
                    Empleado("1", "Juan Pérez", "Cocinero", "Turno Mañana", "Cocina", true),
                    Empleado("2", "Camila Gómez", "Camarera", "Turno Noche", "Salón Principal", true),
                    Empleado("3", "Matías Rossi", "Bartender", "Turno Noche", "Barra", false)
                )
            ),
            onAgregarEmpleadoClick = {}
        )
    }
}
