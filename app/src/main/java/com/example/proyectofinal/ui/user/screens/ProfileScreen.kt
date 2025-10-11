package com.example.proyectofinal.ui.user.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Profile(padding: PaddingValues){
    Box(
        modifier = Modifier.padding(padding)
    ){
        Text(text="Perfil")
    }
}