package com.example.proyectofinal.ui.screens.user.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofinal.ui.screens.user.tabs.Create
import com.example.proyectofinal.ui.screens.user.tabs.Favorites
import com.example.proyectofinal.ui.screens.user.tabs.Map
import com.example.proyectofinal.ui.screens.user.tabs.Profile
import com.example.proyectofinal.ui.screens.user.tabs.Search

@Composable
fun ContentUser(padding: PaddingValues, navController: NavHostController){
    NavHost(
        modifier = Modifier.padding(padding),
        navController=navController,
        startDestination = RouteTab.Map
    ){
        composable<RouteTab.Map>{
            Map()
        }
        composable<RouteTab.Search>{
            Search()
        }
        composable<RouteTab.Create>{
            Create()
        }
        composable<RouteTab.Favorites>{
            Favorites()
        }
        composable<RouteTab.Profile>{
            Profile()
        }
    }
}
