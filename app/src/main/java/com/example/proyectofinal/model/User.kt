package com.example.proyectofinal.model

data class User(
    var id: String = "",
    val nombre: String = "",
    val username: String = "",
    val role: Role = Role.USER,
    val email: String = "",
    val password: String = "",
    val city: City? = City.ARMENIA
){
}