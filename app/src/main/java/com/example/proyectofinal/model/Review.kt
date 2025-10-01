package com.example.proyectofinal.model

data class Review(
    val id: String,
    val userId: String,
    val placeId: String,
    val rating: Int,
    val comment: String,
    val date: String

)