package com.example.proyectofinal.ui.user.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectofinal.ui.navigation.RouteScreen
import com.example.proyectofinal.ui.user.screens.Favorites
import com.example.proyectofinal.ui.user.screens.Map
import com.example.proyectofinal.ui.places.PlaceDetail
import com.example.proyectofinal.ui.theme.BackgroundPrimaryColor
import com.example.proyectofinal.ui.user.screens.Profile
import com.example.proyectofinal.ui.user.screens.Search
import com.example.proyectofinal.utils.SharedPrefsUtil
import com.example.proyectofinal.viewModel.PlacesViewModel

@Composable
fun ContentUser(
    userId: String,
    padding: PaddingValues,
    navController: NavHostController,
    onNavigatePlaceDetail: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    logout: () -> Unit,
    onNavigateToCreatePlace: () -> Unit
){

    val context = LocalContext.current
    NavHost(
        navController=navController,
        startDestination = RouteTab.Map,
        modifier = Modifier.background(BackgroundPrimaryColor),
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
            Profile(
                userId=userId,
                padding=padding,
                logout={
                    logout()
                    SharedPrefsUtil.clearPreferences(context)
                },
                onNavigateToLogin= onNavigateToLogin,
                onNavigatePlaceDetail = onNavigatePlaceDetail,
                onNavigateToCreatePlace = onNavigateToCreatePlace
                )

        }

    }
}
