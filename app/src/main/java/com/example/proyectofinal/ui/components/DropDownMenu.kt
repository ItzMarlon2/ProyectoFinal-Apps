package com.example.proyectofinal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api // Necesario para ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // Para delegados by
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember // 'remember' es suficiente si no necesitas sobrevivir a la recreación por configuración
import androidx.compose.runtime.setValue // Para delegados by
import androidx.compose.ui.Modifier // Buena práctica añadir Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class) // ExposedDropdownMenuBox es experimental
@Composable
fun DropDownMenu(
    items: List<String>,
    text: String,
    selectedItem: String, // El elemento actualmente seleccionado, gestionado por el padre
    onItemSelected: (String) -> Unit, // Callback para notificar al padre de una nueva selección
    modifier: Modifier = Modifier // Añadir un modificador es buena práctica
) {
    var expanded by remember { mutableStateOf(false) } // Usar 'by' para delegados de propiedad

    Column(
        content={
            Text(
                text=text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },

                modifier = modifier // Aplicar el modificador aquí
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(), // Necesario para anclar el menú al TextField
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                        disabledContainerColor = Color.LightGray.copy(alpha = 0.4f),
                        focusedBorderColor = Primary.copy(alpha = 0.4f),
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    value = selectedItem, // <--- CAMBIO CLAVE: Usa el estado del elemento seleccionado
                    onValueChange = {
                        // Esta lambda está aquí por la API, pero como es readOnly
                        // y la selección se maneja en DropdownMenuItem,
                        // no necesitas hacer nada aquí específicamente con 'it'.
                        // La actualización del 'selectedItem' viene de 'onItemSelected'.
                    },
                    supportingText = {},
                    label = {
                        Text(
                            text = stringResource(R.string.register_label_city)
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) { // No necesitas el parámetro 'content =' explícitamente aquí
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                onItemSelected(item) // <--- CAMBIO CLAVE: Notifica al padre
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    )
}
