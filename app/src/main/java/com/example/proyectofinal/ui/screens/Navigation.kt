package com.example.proyectofinal.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.config.RouteScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RouteScreen.Login ,

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
        composable<RouteScreen.Home>{
            HomeScreen()

        }
        composable<RouteScreen.ForgotPassword>{
            ForgotPasswordScreen()

        }

    }
}