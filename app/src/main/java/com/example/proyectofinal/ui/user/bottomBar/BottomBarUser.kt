package com.example.proyectofinal.ui.user.bottomBar


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.user.nav.RouteTab
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryLight
import com.example.proyectofinal.ui.theme.PrimaryUltraLight

@Composable
fun BottomBarUser(navController: NavHostController, onNavigateToCreate: () -> Unit = {}){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar (
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            },
        containerColor = Color.White,
    ){
        Destination.entries.forEachIndexed {
                index, destination ->
            val isSelected = currentDestination?.route == destination.route::class.qualifiedName
            if (destination == Destination.CREATE) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onNavigateToCreate() }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(destination.label),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = stringResource(id = destination.label),
                            color = if (isSelected) Primary else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }else{
                NavigationBarItem(
                    label = {
                        Text(
                            text = stringResource(destination.label)
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        val routeString = destination.route::class.qualifiedName
                        routeString?.let {
                            navController.navigate(it) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(destination.label)
                        )
                    },

                    colors = NavigationBarItemDefaults.colors(Primary, Primary, PrimaryUltraLight, Color.Gray, Color.Gray)
                )
            }

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
    FAVORITES(RouteTab.Favorites, R.string.menu_favorites, Icons.Filled.FavoriteBorder),
    PROFILE(RouteTab.Profile, R.string.menu_profile, Icons.Outlined.Person)

}