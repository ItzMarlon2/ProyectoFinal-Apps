package com.example.proyectofinal.ui.components

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.model.Schedule
import com.example.proyectofinal.ui.theme.Primary
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleDayRow(
    daySchedule: Schedule,
    onDayChange: (Schedule) -> Unit // Lambda para notificar cambios al padre
) {
    val context = LocalContext.current

    // Función para convertir un String "HH:mm" a LocalTime. Devuelve null si el String es nulo o inválido.
    fun stringToLocalTime(timeString: String?): LocalTime? {
        return try {
            timeString?.let { LocalTime.parse(it) }
        } catch (e: Exception) {
            null // En caso de un formato inválido.
        }
    }

    // Función para formatear el String "HH:mm" a un formato legible "hh:mm a".
    fun formatTimeString(timeString: String?): String {
        return stringToLocalTime(timeString)?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "--:--"
    }

    // Función reutilizable para mostrar el diálogo de selección de hora (el "reloj").
    val showTimePicker = { currentTimeString: String?, onTimeSelected: (String) -> Unit ->
        val currentTime = stringToLocalTime(currentTimeString) ?: LocalTime.now()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val newTime = LocalTime.of(hour, minute)
                // Devolvemos la nueva hora como String en formato "HH:mm"
                onTimeSelected(newTime.format(DateTimeFormatter.ofPattern("HH:mm")))
            },
            currentTime.hour,
            currentTime.minute,
            false // 'false' para formato 12h (AM/PM)
        ).show()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Parte izquierda: Checkbox y nombre del día
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1.5f)) {
            Checkbox(
                colors = CheckboxDefaults.colors(Primary),
                checked = daySchedule.isOpen,
                onCheckedChange = { isOpen ->
                    // Al cambiar el checkbox, actualizamos el estado
                    onDayChange(daySchedule.copy(isOpen = isOpen))
                }
            )
            Text(daySchedule.day, style = MaterialTheme.typography.bodyLarge)
        }

        // Parte derecha: Horarios o texto "Cerrado"
        if (!daySchedule.isOpen) {
            Text(
                "Cerrado",
                color = Color.Gray,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.End
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(2f)
            ) {
                // Input para la hora de apertura
                Text(
                    // Usamos la función de formato para mostrar la hora
                    text = formatTimeString(daySchedule.openTime),
                    modifier = Modifier
                        .clickable { // Hacemos el texto clickeable para abrir el reloj
                            showTimePicker(daySchedule.openTime) { newTimeString ->
                                // Guardamos el nuevo String "HH:mm" en el estado
                                onDayChange(daySchedule.copy(openTime = newTimeString))
                            }
                        }
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Text(" - ", modifier = Modifier.padding(horizontal = 4.dp))

                // Input para la hora de cierre
                Text(
                    // Usamos la función de formato para mostrar la hora
                    text = formatTimeString(daySchedule.closeTime),
                    modifier = Modifier
                        .clickable { // Hacemos el texto clickeable para abrir el reloj
                            showTimePicker(daySchedule.closeTime) { newTimeString ->
                                // Guardamos el nuevo String "HH:mm" en el estado
                                onDayChange(daySchedule.copy(closeTime = newTimeString))
                            }
                        }
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
