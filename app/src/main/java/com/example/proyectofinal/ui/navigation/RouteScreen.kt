package com.example.proyectofinal.ui.navigation

import kotlinx.serialization.Serializable

sealed class RouteScreen{
    @Serializable
    data object Login: RouteScreen()
    @Serializable
    data object Register: RouteScreen()
    @Serializable
    data object ForgotPassword: RouteScreen()

    @Serializable
    data object HomeUser: RouteScreen()

    @Serializable
    data object HomeAdmin: RouteScreen()

    @Serializable
    data object CreatePlace: RouteScreen()

    @Serializable
    data class PlaceDetail(val id:String): RouteScreen()
}