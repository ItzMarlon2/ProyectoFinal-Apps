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
    // Almacena la hora como String para compatibilidad con Firestore
    var openTime: String? = null,
    var closeTime: String? = null
) {
    // --- Métodos de ayuda para convertir entre String y LocalTime ---

    /**
     * Obtiene el openTime como un objeto LocalTime.
     * @Exclude previene que Firestore intente mapear este getter.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @Exclude
    fun getOpenTimeAsLocalTime(): LocalTime? {
        return openTime?.let { LocalTime.parse(it) }
    }

    /**
     * Obtiene el closeTime como un objeto LocalTime.
     * @Exclude previene que Firestore intente mapear este getter.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @Exclude
    fun getCloseTimeAsLocalTime(): LocalTime? {
        return closeTime?.let { LocalTime.parse(it) }
    }
}

// --- Función de extensión para formatear (opcional, pero útil) ---

/**
 * Función de extensión para formatear un LocalTime a un String amigable.
 * La puedes usar en tu UI.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime?.toFormattedString(): String {
    return this?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "--:--"
}
