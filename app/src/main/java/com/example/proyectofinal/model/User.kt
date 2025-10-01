package com.example.proyectofinal.model

data class User(
    val id: String,
    val nombre: String,
    val username: String,
    val role: Role,
    val email: String,
    val password: String,
    val city: String
){
}