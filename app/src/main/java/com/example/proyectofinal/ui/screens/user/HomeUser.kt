package com.example.proyectofinal.ui.screens.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.screens.user.bottomBar.BottomBarUser
import com.example.proyectofinal.ui.screens.user.nav.ContentUser

@Composable
fun HomeUser(){
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar ={
            BottomBarUser(navController = navController)
        }
    ){ padding ->
        ContentUser(
            padding=padding,
            navController =navController
        )

    }
}



