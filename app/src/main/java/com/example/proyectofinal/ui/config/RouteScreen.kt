package com.example.proyectofinal.ui.config

import kotlinx.serialization.Serializable

sealed class RouteScreen{
    @Serializable
    data object Home: RouteScreen()
    @Serializable
    data object Login: RouteScreen()
    @Serializable
    data object Register: RouteScreen()
    @Serializable
    data object ForgotPassword: RouteScreen()
}