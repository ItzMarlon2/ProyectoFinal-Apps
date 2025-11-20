package com.example.proyectofinal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.proyectofinal.ui.navigation.Navigation
import com.example.proyectofinal.ui.theme.BackgroundColorFromHSL
import com.example.proyectofinal.ui.theme.MutedColorFromHSL
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.ProyectoFinalTheme
import com.example.proyectofinal.viewModel.MainViewModel
import com.example.proyectofinal.viewModel.PlacesViewModel
import com.example.proyectofinal.viewModel.ReviewsViewModel
import com.example.proyectofinal.viewModel.UsersViewModel

class MainActivity : ComponentActivity() {
    private val usersViewModel: UsersViewModel by viewModels()
    private val reviewsViewModel: ReviewsViewModel by viewModels()
    private val placesViewModel: PlacesViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val mainViewModel = MainViewModel(
            placesViewModel,
            usersViewModel,
            reviewsViewModel
        )
        setContent (

            content ={
                ProyectoFinalTheme {

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                        content = {
                            Navigation(mainViewModel = mainViewModel)
                        }
                    )
                }

            }
            )
    }
}

