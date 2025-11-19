package com.example.proyectofinal.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDateTime

data class Review(
    var id: String = "",
    val userId: String = "",
    val userName: String = "",
    val placeId: String = "",
    val rating: Int = 0,
    val comment: String = "",
    @ServerTimestamp
    val date: Timestamp? = null


)