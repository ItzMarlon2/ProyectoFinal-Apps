package com.example.proyectofinal.ui.user.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.model.User
import com.example.proyectofinal.ui.components.myPlacesList
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.ui.theme.BackgroundPrimaryColor
import com.example.proyectofinal.ui.theme.BorderBoxes
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryUltraLight

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    userId: String,
    padding: PaddingValues,
    logout: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigatePlaceDetail: (String) -> Unit,
    onNavigateToCreatePlace: () -> Unit
) {

    val placesViewModel = localMainViewModel.current.placesViewModel
    val usersViewModel = localMainViewModel.current.usersViewModel
    val currentUser by usersViewModel.currentUser.collectAsState()
    placesViewModel.getMyPlaces(userId)
    val myPlaces by placesViewModel.myPlaces.collectAsState()
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Resumen", "Mis lugares")
    val isRefreshing by placesViewModel.isRefreshing.collectAsState()
    LaunchedEffect(Unit) {
        placesViewModel.getMyPlaces(userId)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        item {
            PrimaryTabRow(
                selectedTabIndex = tabIndex,
                divider = {},
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        color = Color.Transparent
                    )
                }

            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        modifier = Modifier
                            .background(
                                color = if (tabIndex == index) Primary else PrimaryUltraLight,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp)),

                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (tabIndex == index) Color.White else Color.Black
                            )
                        },
                        selectedContentColor = Primary
                    )
                }
            }
        }

        when (tabIndex) {
            0 -> {
                item {
                    currentUser?.let { user ->
                        ContainerUserInfo(user = user)
                    }
                }
            }

            1 -> {
                item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tus lugares", style = MaterialTheme.typography.titleLarge)
                            Button(
                                onClick = { onNavigateToCreatePlace() },
                                colors = ButtonDefaults.buttonColors(Primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(40.dp)
                            ) {
                                Text(
                                    "Crear Lugar",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                                )
                            }
                        }


                }
                item {
                    if(myPlaces.isEmpty()) {

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 100.dp)
                        ) {
                            Text(
                                "Aun no tienes lugares creados",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Icon(
                                imageVector = Icons.Rounded.SentimentDissatisfied,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }

                myPlacesList(
                    places = myPlaces,
                    onNavigatePlaceDetail = onNavigatePlaceDetail,
                    isRefreshing = isRefreshing,
                    onRefresh = { placesViewModel.getMyPlaces(currentUser!!.id) }
                )

            }
        }

        item { ContainerOtherAccess(logout = logout, onNavigateToLogin = onNavigateToLogin) }
    }
}


@Composable
fun ContainerUserInfo( user: User){
    Column (
        modifier = Modifier
            .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp)).background(Color.White).padding(25.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier.weight(0.7f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ){
                Box(
                    modifier = Modifier.size(80.dp)
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Primary),
                        contentAlignment = Alignment.Center

                    ){
                        if (user.nombre.isNotEmpty()) {
                            Text(text = user.nombre.first().uppercase(), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Primary),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(imageVector = Icons.Outlined.CameraAlt, contentDescription = "Camera", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = user.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text="@${user.username}", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    user.city?.let { city ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = "city", tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Text(text = city.displayName, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.Center
            ){
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(BackgroundPrimaryColor, Color.Black),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(imageVector = Icons.Outlined.ModeEdit, contentDescription = "edit")
                    }
                }
            }
        }
    }
}

@Composable
fun ContainerOtherAccess(logout: () -> Unit, onNavigateToLogin: () -> Unit){
    Column (
        modifier = Modifier
            .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp)).background(Color.White).padding(25.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ){
        Button(
            onClick = {
                logout()
                onNavigateToLogin()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Red, ),
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,

            ){
                Icon(imageVector = Icons.Outlined.Logout, contentDescription = "Logout")
                Text(text = "Cerrar sesión", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = Color.Red)
            }
        }
    }
}
