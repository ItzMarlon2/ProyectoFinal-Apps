package com.example.proyectofinal.ui.screens.user.nav

import kotlinx.serialization.Serializable

sealed class RouteTab {
    @Serializable
    data object Map: RouteTab()
    @Serializable
    data object Search: RouteTab()
    @Serializable
    data object Create: RouteTab()
    @Serializable
    data object Favorites: RouteTab()
    @Serializable
    data object Profile: RouteTab()
}