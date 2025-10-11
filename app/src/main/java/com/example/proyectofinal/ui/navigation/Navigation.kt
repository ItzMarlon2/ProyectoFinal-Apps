package com.example.proyectofinal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
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
import com.example.proyectofinal.ui.user.nav.RouteTab
import com.example.proyectofinal.utils.SharedPrefsUtil
import com.example.proyectofinal.viewModel.MainViewModel
import com.example.proyectofinal.viewModel.PlacesViewModel
import com.example.proyectofinal.viewModel.UsersViewModel

val localMainViewModel =  staticCompositionLocalOf<MainViewModel> {error("MainViewModel not provided")}
@Composable
fun Navigation(mainViewModel: MainViewModel){

    val context = LocalContext.current
    val navController = rememberNavController()
    val user = SharedPrefsUtil.getPreferences(context)

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
            startDestination = startDestination ,

            ){
            composable<RouteScreen.Login>{
                LoginForm(
                    onNavigateToRegister = { navController.navigate(RouteScreen.Register) },
                    onNavigateToForgotPassword = { navController.navigate(RouteScreen.ForgotPassword) },
                    onNavigateToHome = {userId, role ->
                        SharedPrefsUtil.savePreferences(context, userId, role)
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
                HomeUser(onNavigatePlaceDetail ={
                    navController.navigate(RouteScreen.PlaceDetail(it))
                }, navController)

            }

            composable<RouteScreen.CreatePlace>{
                CreatePlaceScreen(

                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )

            }

            composable<RouteScreen.PlaceDetail>{
                val args = it.toRoute<RouteScreen.PlaceDetail>()
                PlaceDetail(id = args.id )
            }

        }
    }

}