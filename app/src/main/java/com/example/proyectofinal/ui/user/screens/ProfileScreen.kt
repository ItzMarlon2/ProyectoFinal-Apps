package com.example.proyectofinal.ui.user.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.ui.theme.BackgroundPrimaryColor
import com.example.proyectofinal.ui.theme.BorderBoxes
import com.example.proyectofinal.ui.theme.Primary

@Composable
fun Profile(padding: PaddingValues, logout: () -> Unit, onNavigateToLogin: () -> Unit){

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        ContainerUserInfo()
        ContainerOtherAccess(logout=logout, onNavigateToLogin=onNavigateToLogin)
    }
}

@Composable
fun ContainerUserInfo(){
    Column (
        modifier = Modifier
            .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp)).background(Color.White).padding(25.dp).fillMaxWidth().height(300.dp),
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
                        Text(text = "M", color = Color.White, fontSize = 20.sp)
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
                    Text(text = "Marlon Campo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text="@ItzMarlon13", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    Row {
                        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = "city", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Text(text="Armenia", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
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