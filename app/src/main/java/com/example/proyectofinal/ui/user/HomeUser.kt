package com.example.proyectofinal.ui.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.navigation.RouteScreen
import com.example.proyectofinal.ui.user.bottomBar.BottomBarUser
import com.example.proyectofinal.ui.user.nav.ContentUser

@Composable
fun HomeUser(
    onNavigatePlaceDetail: (String) -> Unit,
    mainNavController: NavHostController
){
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar ={
            BottomBarUser(navController = navController, onNavigateToCreate = {mainNavController.navigate(
                RouteScreen.CreatePlace::class.qualifiedName!!)})
        }
    ){ padding ->
        ContentUser(
            padding=padding,
            navController =navController,
            onNavigatePlaceDetail=onNavigatePlaceDetail
        )

    }
}



