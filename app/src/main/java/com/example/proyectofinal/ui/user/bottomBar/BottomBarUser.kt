package com.example.proyectofinal.ui.user.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.navigation.RouteScreen
import com.example.proyectofinal.ui.user.nav.RouteTab
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryLight

@Composable
fun BottomBarUser(navController: NavHostController, onNavigateToCreate: () -> Unit = {}){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar (

    ){
        Destination.entries.forEachIndexed {
                index, destination ->
            val isSelected = currentDestination?.route == destination.route::class.qualifiedName

            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(destination.label)
                    )
                },
                selected = isSelected,
                onClick = {
                    if (destination == Destination.CREATE) {
                        // 2. LLAMA AL NUEVO CALLBACK EN LUGAR DE NAVEGAR DIRECTAMENTE
                        onNavigateToCreate()
                    } else {
                        // La navegación entre pestañas sigue igual
                        val routeString = destination.route::class.qualifiedName
                        routeString?.let {
                            navController.navigate(it) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                colors = NavigationBarItemDefaults.colors(Primary, PrimaryLight, Color.Transparent, Color.Black)
            )
        }

    }
}

enum class Destination(
    val route: RouteTab,
    val label: Int,
    val icon: ImageVector,
){
    MAP(RouteTab.Map, R.string.menu_map, Icons.Outlined.Map),
    SEARCH(RouteTab.Search, R.string.menu_search, Icons.Outlined.Search),
    CREATE(RouteTab.Create, R.string.menu_create, Icons.Outlined.Add),
    FAVORITES(RouteTab.Favorites, R.string.menu_favorites, Icons.Outlined.Favorite),
    PROFILE(RouteTab.Profile, R.string.menu_profile, Icons.Outlined.Person)

}