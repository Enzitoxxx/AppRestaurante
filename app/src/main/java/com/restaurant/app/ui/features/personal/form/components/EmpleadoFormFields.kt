package com.restaurant.app.ui.features.personal.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppTextField
import com.restaurant.app.ui.features.personal.form.EmpleadoFormUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoFormFields(
    state: EmpleadoFormUiState,
    onNombreChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onTurnoChange: (String) -> Unit = {},
    onAsignacionChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expandidoRol by remember { mutableStateOf(false) }
    var expandidoTurno by remember { mutableStateOf(false) }
    var expandidoAsignacion by remember { mutableStateOf(false) }

    val roles = listOf("Camarero / Mozo", "Cocinero", "Bartender")
    val turnos = listOf("Turno Mañana", "Turno Tarde", "Turno Noche", "Turno Cortado")

    // Opciones de Asignación / Zona dinámicas según el Rol elegido
    val asignaciones = when {
        state.rol.contains("Camarer", ignoreCase = true) || state.rol.contains("Mozo", ignoreCase = true) -> {
            listOf("Salón Principal", "Patio / Terraza")
        }
        state.rol.contains("Bartender", ignoreCase = true) -> {
            listOf("Barra")
        }
        state.rol.contains("Cocinero", ignoreCase = true) -> {
            listOf("Cocina")
        }
        else -> listOf("Salón Principal", "Patio / Terraza", "Barra", "Cocina")
    }
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. NOMBRE Y APELLIDO (Reutilizando AppTextField)
        AppTextField(
            label = stringResource(R.string.empleado_form_label_nombre),
            value = state.nombre,
            onValueChange = onNombreChange,
            errorText = state.errorNombre?.let { stringResource(it) },
            enabled = !state.estaCargando,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )

        // 2. TELÉFONO MÓVIL (Reutilizando AppTextField)
        AppTextField(
            label = stringResource(R.string.empleado_form_label_telefono),
            value = state.telefono,
            onValueChange = onTelefonoChange,
            errorText = state.errorTelefono?.let { stringResource(it) },
            enabled = !state.estaCargando,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        // 3. CORREO ELECTRÓNICO (Reutilizando AppTextField)
        AppTextField(
            label = stringResource(R.string.empleado_form_label_correo),
            value = state.correo,
            onValueChange = onCorreoChange,
            errorText = state.errorCorreo?.let { stringResource(it) },
            enabled = !state.estaCargando,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        // 4. ROL ASIGNADO (Dropdown Selector)
        ExposedDropdownMenuBox(
            expanded = expandidoRol,
            onExpandedChange = { expandidoRol = !expandidoRol },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.rol,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.empleado_form_label_rol)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoRol) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    focusedLabelColor = colors.primary
                )
            )

            ExposedDropdownMenu(
                expanded = expandidoRol,
                onDismissRequest = { expandidoRol = false }
            ) {
                roles.forEach { opcionRol ->
                    DropdownMenuItem(
                        text = { Text(opcionRol) },
                        onClick = {
                            onRolChange(opcionRol)
                            expandidoRol = false
                        }
                    )
                }
            }
        }

        // 5. TURNO DE TRABAJO (Dropdown Selector)
        ExposedDropdownMenuBox(
            expanded = expandidoTurno,
            onExpandedChange = { expandidoTurno = !expandidoTurno },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.turno,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.empleado_form_label_turno)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoTurno) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    focusedLabelColor = colors.primary
                )
            )

            ExposedDropdownMenu(
                expanded = expandidoTurno,
                onDismissRequest = { expandidoTurno = false }
            ) {
                turnos.forEach { opcionTurno ->
                    DropdownMenuItem(
                        text = { Text(opcionTurno) },
                        onClick = {
                            onTurnoChange(opcionTurno)
                            expandidoTurno = false
                        }
                    )
                }
            }
        }

        // 6. ZONA / ESTACIÓN DE TRABAJO (Dropdown Selector)
        ExposedDropdownMenuBox(
            expanded = expandidoAsignacion,
            onExpandedChange = { expandidoAsignacion = !expandidoAsignacion },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.asignacion,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.empleado_form_label_asignacion)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoAsignacion) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    focusedLabelColor = colors.primary
                )
            )

            ExposedDropdownMenu(
                expanded = expandidoAsignacion,
                onDismissRequest = { expandidoAsignacion = false }
            ) {
                asignaciones.forEach { opcionAsignacion ->
                    DropdownMenuItem(
                        text = { Text(opcionAsignacion) },
                        onClick = {
                            onAsignacionChange(opcionAsignacion)
                            expandidoAsignacion = false
                        }
                    )
                }
            }
        }
    }
}
