// En: com/example/proyectofinal/model/Schedule.kt

package com.example.proyectofinal.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.Exclude
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Schedule(
    val day: String = "",
    var isOpen: Boolean = true,
    var openTime: String? = null,
    var closeTime: String? = null
) {
    @RequiresApi(Build.VERSION_CODES.O)
    @Exclude
    fun getOpenTimeAsLocalTime(): LocalTime? {
        return openTime?.let { LocalTime.parse(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Exclude
    fun getCloseTimeAsLocalTime(): LocalTime? {
        return closeTime?.let { LocalTime.parse(it) }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime?.toFormattedString(): String {
    return this?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "--:--"
}
