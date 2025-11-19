package com.example.proyectofinal.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectofinal.model.Role
import com.example.proyectofinal.ui.auth.ForgotPasswordScreen
import com.example.proyectofinal.ui.auth.LoginForm
import com.example.proyectofinal.ui.auth.RegisterScreen
import com.example.proyectofinal.ui.user.HomeUser
import com.example.proyectofinal.ui.places.CreatePlaceScreen
import com.example.proyectofinal.ui.places.PlaceDetail
import com.example.proyectofinal.utils.SharedPrefsUtil
import com.example.proyectofinal.viewModel.MainViewModel
import com.example.proyectofinal.viewModel.UsersViewModel
import kotlin.math.log

val localMainViewModel =  staticCompositionLocalOf<MainViewModel> {error("MainViewModel not provided")}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(mainViewModel: MainViewModel, usersViewModel: UsersViewModel = viewModel()){

    val context = LocalContext.current
    val navController = rememberNavController()
    var user by remember { mutableStateOf(SharedPrefsUtil.getPreferences(context)) }
    println("usuario: $user")
    val startDestination=if(user.isEmpty()){
        RouteScreen.Login
    }else{
        if(user["role"] == "ADMIN"){
            RouteScreen.HomeAdmin
        }else{
            RouteScreen.HomeUser
        }
    }

    CompositionLocalProvider(localMainViewModel provides mainViewModel) {
        NavHost(
            navController = navController,
            startDestination = startDestination,

            ){
            composable<RouteScreen.Login>{
                LoginForm(
                    onNavigateToRegister = { navController.navigate(RouteScreen.Register) },
                    onNavigateToForgotPassword = { navController.navigate(RouteScreen.ForgotPassword) },
                    onNavigateToHome = {userId, role ->
                        SharedPrefsUtil.savePreferences(context, userId, role)
                        user = SharedPrefsUtil.getPreferences(context)
                        if(role == Role.ADMIN){
                            navController.navigate(RouteScreen.HomeAdmin)
                        }else{
                            navController.navigate(RouteScreen.HomeUser)
                        }
                    },

                )
            }
            composable<RouteScreen.Register>{
                RegisterScreen(
                    onNavigateToLogin = { navController.navigate(RouteScreen.Login) },
                )
            }
            composable<RouteScreen.ForgotPassword>{
                ForgotPasswordScreen()

            }

            composable<RouteScreen.HomeUser>{
                HomeUser(

                    userId = user["userId"]!!,
                    onNavigatePlaceDetail ={
                    navController.navigate(RouteScreen.PlaceDetail(it))
                }, onNavigateToLogin = {
                    navController.navigate(RouteScreen.Login){
                        popUpTo(navController.graph.startDestinationId){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                    navController,
                    logout ={
                        usersViewModel.logOut()
                    },
                    onNavigateToCreatePlace = {
                        navController.navigate(RouteScreen.CreatePlace)
                    }
                    )

            }

            composable<RouteScreen.CreatePlace>{
                CreatePlaceScreen(
                    userId = user["userId"] ?: "",
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )

            }

            composable<RouteScreen.PlaceDetail>{
                val args = it.toRoute<RouteScreen.PlaceDetail>()

                PlaceDetail(placeId = args.id, userId = user["userId"] ?: "" , onNavigateBack = {
                    navController.popBackStack()
                })
            }

            composable<RouteScreen.HomeAdmin>{}

        }
    }

}