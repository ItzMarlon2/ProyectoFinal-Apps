package com.example.proyectofinal.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Place(
    var id: String = "",
    val title: String= "",
    val description: String= "",
    val address: String= "",
    val location: Location = Location(),
    val images: List<String> = emptyList<String>(),
    val phones: List<String> = emptyList<String>(),
    val type: PlaceType? = PlaceType.DEFAULT,
    val schedules: List<Schedule> = emptyList<Schedule>(),
    val ownerId: String= "",
    val city: City? = City.POR_DEFECTO,
    val website: String= "",
    val favoritedBy: List<String> = listOf(),
    @get:Exclude var reviews: List<Review> = emptyList(),
    @ServerTimestamp
    val creationDate: Timestamp? = null,


    )