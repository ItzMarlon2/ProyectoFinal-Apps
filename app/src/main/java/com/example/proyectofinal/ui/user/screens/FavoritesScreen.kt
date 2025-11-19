package com.example.proyectofinal.ui.user.screens
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.proyectofinal.ui.components.PlacesList
import com.example.proyectofinal.ui.navigation.localMainViewModel

@Composable
fun Favorites(padding: PaddingValues, onNavigatePlaceDetail: (String) -> Unit){
    val placesViewModel= localMainViewModel.current.placesViewModel
    val places by placesViewModel.places.collectAsState()
    val isRefreshing by placesViewModel.isRefreshing.collectAsState()
    LaunchedEffect(Unit) {
        placesViewModel.getAll()
    }

    PlacesList(
        places = places,
        padding = padding,
        onNavigatePlaceDetail = onNavigatePlaceDetail,
        isRefreshing  = isRefreshing,
        onRefresh = {placesViewModel.getAll()}
    )
}