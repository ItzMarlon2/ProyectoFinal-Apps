package com.example.proyectofinal.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.config.RouteScreen
import com.example.proyectofinal.ui.screens.user.HomeUser

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RouteScreen.HomeUser ,

    ){
        composable<RouteScreen.Login>{
            LoginForm(
                onNavigateToRegister = {navController.navigate(RouteScreen.Register)},
                onNavigateToForgotPassword = {navController.navigate(RouteScreen.ForgotPassword)}
            )
        }
        composable<RouteScreen.Register>{
            RegisterScreen(
                onNavigateToLogin = {navController.navigate(RouteScreen.Login)}
            )
        }
        composable<RouteScreen.ForgotPassword>{
            ForgotPasswordScreen()

        }

        composable<RouteScreen.HomeUser>{
            HomeUser()

        }

    }
}