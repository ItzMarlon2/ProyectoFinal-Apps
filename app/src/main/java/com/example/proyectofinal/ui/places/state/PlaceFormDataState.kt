package com.example.proyectofinal.ui.places.state

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.proyectofinal.model.City
import com.example.proyectofinal.model.PlaceType
import com.mapbox.geojson.Point
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import com.example.proyectofinal.model.Schedule
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
val PlaceFormDataSaver = mapSaver(
    save = { formData ->
        val savableSchedules = formData.schedule.map { daySchedule ->
            mapOf(
                "day" to daySchedule.day,
                "isOpen" to daySchedule.isOpen,
                "openTime" to daySchedule.openTime,
                "closeTime" to daySchedule.closeTime
            )
        }
        mapOf(
            "name" to formData.name,
            "description" to formData.description,
            "category" to formData.category?.name,
            "loc_lat" to formData.location?.latitude(),
            "loc_lon" to formData.location?.longitude(),
            "phone" to formData.phone,
            "website" to formData.website,
            "city" to formData.city?.name,
            "address" to formData.address,
            "images" to formData.images,
            "schedules" to savableSchedules
        )
    },
    restore = { map ->
        val restoredSchedules = (map["schedules"] as? List<Map<String, Any?>>)?.map { scheduleMap ->
            Schedule(
                day = scheduleMap["day"] as String,
                isOpen = scheduleMap["isOpen"] as Boolean,
                openTime = scheduleMap["openTime"] as? String,
                closeTime = scheduleMap["closeTime"] as? String
            )
        } ?: emptyList()

        PlaceFormData(
            name = map["name"] as String,
            description = map["description"] as String,
            category = (map["category"] as? String)?.let { enumName -> PlaceType.valueOf(enumName) },
            location = if (map["loc_lat"] != null && map["loc_lon"] != null) {
                Point.fromLngLat(map["loc_lon"] as Double, map["loc_lat"] as Double)
            } else {
                null
            },
            phone = map["phone"] as String,
            website = map["website"] as String,
            city = (map["city"] as? String)?.let { enumName -> City.valueOf(enumName) },
            address = map["address"] as String,
            images = (map["images"] as? List<String>) ?: emptyList(),
            schedule = restoredSchedules
        )
    }
)



data class PlaceFormData(
    val name: String = "",
    val description: String = "",
    val category: PlaceType? = null,
    val location: Point? = null,
    val phone: String = "",
    val website: String = "",
    val city: City? = null,
    val address: String = "",
    val images: List<String> = emptyList(),
    val schedule: List<Schedule> = emptyList(),
)