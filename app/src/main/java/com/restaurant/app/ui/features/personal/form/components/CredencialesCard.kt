package com.restaurant.app.ui.features.personal.form.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.restaurant.app.R

@Composable
fun CredencialesCard(
    usuario: String,
    claveTemporal: String,
    modifier: Modifier = Modifier
) {
    if (usuario.isBlank()) return

    val colorMentaSuave = Color(0xFFE8F5E9)
    val colorBordeMenta = Color(0xFFA5D6A7)
    val colorVerdeOscuro = Color(0xFF1B5E20)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colorMentaSuave),
        border = BorderStroke(1.dp, colorBordeMenta),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título con Icono Llave 🔑
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Key,
                    contentDescription = null,
                    tint = colorVerdeOscuro,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = stringResource(R.string.empleado_form_credenciales_titulo),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorVerdeOscuro
                )
            }

            Spacer(Modifier.height(12.dp))

            // Caja blanca interna con monospaced font
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Row {
                        Text(
                            text = "Usuario: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = usuario,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00695C)
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Row {
                        Text(
                            text = "Clave temporal: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = claveTemporal,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF00695C)
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Subtítulo WhatsApp
            Text(
                text = stringResource(R.string.empleado_form_credenciales_whatsapp),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF2E7D32)
            )
        }
    }
}
