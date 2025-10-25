package com.example.proyectofinal.viewModel

import androidx.lifecycle.ViewModel
import com.example.proyectofinal.model.Location
import com.example.proyectofinal.model.Place
import com.example.proyectofinal.model.PlaceType
import com.example.proyectofinal.model.Schedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

class PlacesViewModel: ViewModel() {
    private val _places= MutableStateFlow(emptyList<Place>())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    init{
        loadPlaces()
    }

    fun loadPlaces(){
        _places.value = listOf(
            Place(
                id = "1",
                title = "Restaurante El paisa",
                description = "El mejor restaurante de bogota",
                address = "calle 123",
                location = Location(1.23, 2.34),
                images = listOf("https://plazaclaro.com.co/wp-content/uploads/2022/11/EL-PAISA.png",
                    "https://static.spotapps.co/website_images/ab_websites/298841_website_v1/logo.png",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0MbKRp96EmTCTmOY7VRRJu6qYj0TRYcIo5A&s",),
                phones = listOf("123"),
                type = PlaceType.RESTAURANTE,
                schedules = listOf(
                    Schedule(DayOfWeek.MONDAY, LocalTime(10, 0), LocalTime(20, 0)),

                ),
                ownerId = "1"
            ),
            Place(
                id = "2",
                title = "Bar test",
                description = "El mejor bar de bogota",
                address = "calle 123",
                location = Location(1.23, 2.34),
                images = listOf("https://cdn-blog.reservandonos.com/blog/wp-content/uploads/2024/10/07161642/mejores-bares-colombia.webp"),
                phones = listOf("123"),
                type = PlaceType.BAR,
                schedules = listOf(),
                ownerId = "2"
            ),
        )
    }

    fun create(place: Place){
        _places.value = _places.value + place
    }

    fun findById(id: String): Place?{
        return _places.value.find { it.id == id }
    }

    fun findByType(type: PlaceType): List<Place>{
        return _places.value.filter { it.type == type }
    }

    fun findByName(name: String): List<Place>{
        return _places.value.filter { it.title.contains(name, ignoreCase = true) }
    }
}