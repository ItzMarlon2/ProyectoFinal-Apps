package com.example.proyectofinal.ui.places

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.viewModel.PlacesViewModel

@Composable
fun PlaceDetail (id: String){
    val placesViewModel = localMainViewModel.current.placesViewModel
    val place = placesViewModel.findById(id)

    Box(
    ){
        Text(
            text = id
        )
    }
}