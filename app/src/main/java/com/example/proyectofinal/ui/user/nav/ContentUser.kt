package com.example.proyectofinal.ui.user.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectofinal.ui.user.screens.Favorites
import com.example.proyectofinal.ui.user.screens.Map
import com.example.proyectofinal.ui.places.PlaceDetail
import com.example.proyectofinal.ui.user.screens.Profile
import com.example.proyectofinal.ui.user.screens.Search
import com.example.proyectofinal.viewModel.PlacesViewModel

@Composable
fun ContentUser(padding: PaddingValues, navController: NavHostController, onNavigatePlaceDetail: (String) -> Unit){


    NavHost(
        navController=navController,
        startDestination = RouteTab.Map
    ){

        composable<RouteTab.Map>{
            Map(padding = padding)
        }
        composable<RouteTab.Search>{
            Search(
                padding = padding,
                onNavigatePlaceDetail = onNavigatePlaceDetail
            )
        }
        composable<RouteTab.Favorites>{
            Favorites(padding = padding,  onNavigatePlaceDetail = onNavigatePlaceDetail)
        }
        composable<RouteTab.Profile>{
            Profile(padding=padding)
        }

    }
}
