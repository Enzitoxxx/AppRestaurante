package com.restaurant.app.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.restaurant.app.R

@Composable
fun HomeHeader(
    nombreRestaurante: String,
    inicialesUsuario: String = "LF",
    onNotificacionesClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.home_panel_admin),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp
            )
            Text(
                text = nombreRestaurante,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        IconButton(
            onClick = onNotificacionesClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = stringResource(R.string.cd_notifications),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = inicialesUsuario,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    /** Esta es una funcion que crea la parte superior de la pantalla principal
     * La realizamos con Compose como todas las demas. Nos permite construir una pantalla
     * combinando componentes.

     modifier --> Permite personalizar el diseño o el tamaño del componente desde donde lo utilizamos
     Ej: Creamos un boton: Button(onClick = {}) { Text("Guardar") }
     Ahora supongamos que ese boton queremos que ocupe tod el ancho de la pantalla, que tenga un
     tamaño especifico o tenga un margen.
     Para eso utilizamos "MODIFIER" :
     Button(onClick = {} modifier = Modifier.fillMaxWidth()) { Text("Guardar") }

     ¿Y por qué en el HomeHeader aparece?
     Porque significa que la funcion esta "HomeHeader" puede recibir un Modifier desde
     afuera.
     Ej: Si utilizamos la funcion podemos decidir como ubicar o que modificacioens aplicarle a ese
     boton sin tenerque cambiar su codigo interno.

    () -> Unit --> Significa que recibe una función que no necesita parámetros y no devuelve ningún resultado.
     */

}