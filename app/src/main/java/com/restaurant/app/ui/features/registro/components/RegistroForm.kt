package com.restaurant.app.ui.features.registro.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppTextField
import com.restaurant.app.core.designsystem.PrimaryButton
import com.restaurant.app.ui.features.registro.RegistroUiState

//Aqui reutilizamos AppTextField con sus 5 campos y el botn de PrimaryButton
@Composable
fun RegistroForm(
    state: RegistroUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onRestauranteChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onToggleContrasenaVisibility: () -> Unit,
    onRegistroClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Nombre y Apellido
        AppTextField(
            label = stringResource(R.string.registro_nombre_etiqueta),
            value = state.nombre,
            onValueChange = onNombreChange,
            leadingIcon = Icons.Outlined.Person,
            errorText = state.errorNombre?.let { stringResource(it) },
            enabled = !state.estaCargado,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )

        // 2. Correo Electrónico
        AppTextField(
            label = stringResource(R.string.login_email_label),
            value = state.correo,
            onValueChange = onCorreoChange,
            leadingIcon = Icons.Outlined.Email,
            errorText = state.errorCorreo?.let { stringResource(it) },
            enabled = !state.estaCargado,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        // 3. Teléfono Móvil
        AppTextField(
            label = stringResource(R.string.registro_telefono_etiqueta),
            value = state.telefono,
            onValueChange = onTelefonoChange,
            leadingIcon = Icons.Outlined.Phone,
            errorText = state.errorTelefono?.let { stringResource(it) },
            enabled = !state.estaCargado,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )

        // 4. Nombre del Restaurante
        AppTextField(
            label = stringResource(R.string.registro_restaurante_etiqueta),
            value = state.restaurante,
            onValueChange = onRestauranteChange,
            leadingIcon = Icons.Outlined.Storefront,
            errorText = state.errorRestaurante?.let { stringResource(it) },
            enabled = !state.estaCargado,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )

        // 5. Contraseña
        AppTextField(
            label = stringResource(R.string.login_password_label),
            value = state.contrasena,
            onValueChange = onContrasenaChange,
            leadingIcon = Icons.Outlined.Key,
            errorText = state.errorContrasena?.let { stringResource(it) },
            enabled = !state.estaCargado,
            trailingContent = {
                IconButton(onClick = onToggleContrasenaVisibility) {
                    Icon(
                        imageVector = if (state.contrasenaVisible) Icons.Outlined.VisibilityOff
                        else Icons.Outlined.Visibility,
                        contentDescription = stringResource(
                            if (state.contrasenaVisible) R.string.cd_hide_password
                            else R.string.cd_show_password
                        ),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            visualTransformation = if (state.contrasenaVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onRegistroClick() })
        )

        // Botón Principal "Crear cuenta"
        PrimaryButton(
            text = stringResource(R.string.registro_boton),
            onClick = onRegistroClick,
            isLoading = state.estaCargado,
            trailingIcon = Icons.Outlined.CheckCircle
        )
    }
}