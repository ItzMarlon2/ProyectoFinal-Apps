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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Schedule
import com.example.proyectofinal.ui.theme.Primary
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleDayRow(
    daySchedule: Schedule,
    onDayChange: (Schedule) -> Unit
) {
    val context = LocalContext.current

    fun stringToLocalTime(timeString: String?): LocalTime? {
        return try {
            timeString?.let { LocalTime.parse(it) }
        } catch (e: Exception) {
            null
        }
    }

    fun formatTimeString(timeString: String?): String {
        return stringToLocalTime(timeString)?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "--:--"
    }

    val showTimePicker = { currentTimeString: String?, onTimeSelected: (String) -> Unit ->
        val currentTime = stringToLocalTime(currentTimeString) ?: LocalTime.now()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val newTime = LocalTime.of(hour, minute)
                onTimeSelected(newTime.format(DateTimeFormatter.ofPattern("HH:mm")))
            },
            currentTime.hour,
            currentTime.minute,
            false
        ).show()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1.5f)) {
            Checkbox(
                colors = CheckboxDefaults.colors(Primary),
                checked = daySchedule.isOpen,
                onCheckedChange = { isOpen ->
                    onDayChange(daySchedule.copy(isOpen = isOpen))
                }
            )
            Text(daySchedule.day, style = MaterialTheme.typography.bodyLarge)
        }

        if (!daySchedule.isOpen) {
            Text(
                stringResource(R.string.cerrado),
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
                Text(
                    text = formatTimeString(daySchedule.openTime),
                    modifier = Modifier
                        .clickable {
                            showTimePicker(daySchedule.openTime) { newTimeString ->
                                onDayChange(daySchedule.copy(openTime = newTimeString))
                            }
                        }
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Text(" - ", modifier = Modifier.padding(horizontal = 4.dp))

                Text(
                    text = formatTimeString(daySchedule.closeTime),
                    modifier = Modifier
                        .clickable {
                            showTimePicker(daySchedule.closeTime) { newTimeString ->
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
