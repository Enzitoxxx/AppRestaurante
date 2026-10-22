package com.restaurant.app.ui.features.forgotpassword

// dibuja la pantalla y conecta las acciones con el ViewModel


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurant.app.R
import com.restaurant.app.core.designsystem.AppTextField
import com.restaurant.app.core.designsystem.PrimaryButton
import com.restaurant.app.ui.theme.AppTheme


// Esta parte conecta la pantalla con el ViewModel

// CONECTA LA PANTALLA CON EL VIEWMODEL
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,  //funcion que se ejectua al volver atras
    onEmailSent: () -> Unit,  // funcion que se ejecuta cuando se completa el envio simulado
    viewModel: ForgotPasswordViewModel = viewModel()  //contiene la logica de la pantalla

    // Que significa "() -> Unit" ?
    //Significa que recibimos una funcion que no necesita parametros y no devuelve ningun resultado

){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    /*
    "viewModel.uiState" --> obtiene el estado de la pantalla
    "collectAsStateWithLifecycle()" --> observa los cambios de ese estado
    "val state" --> guarda el estado observado en una variable llamada state
    "by" --> permite acceder directamente la valor, sin tener que escribir ".value"

    Ej: si el usuario escribe su correo, state.email se actualiza y la pantalla muestra el nuevo texto*/


    // Esta parte escucha los avisos que envia el ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ForgotPasswordEvent.ResetPasswordEmailSent -> onEmailSent()
            }
        }
    }
    /*
    "LaunchedEffect" --> sirve para ejecutar una tarea mientras la pantalla esta activa.
    En este caso, escuchas los avisos del ViewModel
    "collect" --> se que escuchando los eventos que envia el ViewModel, Cuando llega uno.. lo guarda
    temporalmente en la variable "event"
    */

    ForgotPasswordContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onSendClick = viewModel::onSendClick,
        onBackClick = onBackClick
    )
    /*
    Le pasa a la interfaz tod lo que necesita:
    -- state: los datos actuales, como el correo y los errores.
    -- onEmailChange: qué hacer cuando se escribe en el campo.
    -- onSendClick: qué hacer cuando se toca el botón.
    --onBackClick: qué hacer al volver atrás.

    El símbolo :: permite pasar una función que ya existe, sin ejecutarla en ese momento.
    */
}

/** Renderiza la UI a partir del estado. Sin ViewModel para fácil Preview. */

// CONTIENE LOS ELEMENTOS DE LA PANTALLA
@Composable
fun ForgotPasswordContent(
    state: ForgotPasswordUiState, //para saber que correo mostrar y si hay errores
    onEmailChange: (String) -> Unit, //para avisar cuando escribimos el correo
    onSendClick: () -> Unit, //para avisar cuando tocamos enviar
    onBackClick: () -> Unit, //para volver atras
    modifier: Modifier = Modifier //para permitir modificar como se muestra la pantalla
    // o sea "modifier" sirve para cambiar como se muestra o se comporta un elemento en la pantalla
    //EJ: cambiar su tamaño, agregarle espacio o ponerle un fondo.
    // "= Modifier" signfica que, si no le pasamos ningun cambio, utilizar el valor predterminado
){
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
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
              .padding(horizontal = 24.dp, vertical = 24.dp)

      ) {
          // BOTON PARA REGRESAR
          IconButton(
              onClick = onBackClick,
              modifier = Modifier
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = stringResource(R.string.cd_back),
                  tint = MaterialTheme.colorScheme.onSurface
              )
          }
          Spacer(Modifier.height(32.dp))

          // Icono decorativo del sobre/correo
          Box(
              modifier = Modifier
                  .size(56.dp)
                  .clip(MaterialTheme.shapes.medium)
                  .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
          ){
              Icon(
                  imageVector = Icons.Outlined.MarkEmailRead,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(28.dp)
              )
          }
          Spacer(Modifier.height(20.dp))

          // Título
          Text(
              text = stringResource(R.string.forgot_password_title),
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
          )

          Spacer(Modifier.height(8.dp))

          // Subtítulo
          Text(
              text = stringResource(R.string.forgot_password_subtitle),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(Modifier.height(32.dp))

          // Campo de texto de correo
          AppTextField(
              label = stringResource(R.string.forgot_password_email_label),
              value = state.email,
              onValueChange = onEmailChange,
              leadingIcon = Icons.Outlined.Email,
              errorText = state.emailError?.let { stringResource(it) },
              keyboardOptions = KeyboardOptions(
                  keyboardType = KeyboardType.Email,
                  imeAction = ImeAction.Done
              ),
              keyboardActions = KeyboardActions(onDone = { onSendClick() })
          )

          Spacer(Modifier.height(24.dp))

          // Botón principal
          PrimaryButton(
              text = stringResource(R.string.forgot_password_button),
              onClick = onSendClick,
              isLoading = state.isLoading,
              trailingIcon = Icons.AutoMirrored.Outlined.Send
          )
      }
    }
}

//Permite ver una vista previa de la pantalla en Android Studio, con el fondo y la barra del sistema.
@Preview(showBackground = true, showSystemUi = true)

// Crea una función privada que se utiliza para mostrar esa vista previa.
@Composable
private fun ForgotPasswordScreenPreview() {

    // Aplica el tema visual de la aplicación: colores, estilos, etc.
    AppTheme {
        ForgotPasswordContent(
            state = ForgotPasswordUiState(email = "lucia@labuenamesa.com.ar"),
            onEmailChange = {},
            onSendClick = {},
            onBackClick = {}
        )
    }
}