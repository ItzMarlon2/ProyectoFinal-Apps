package com.example.proyectofinal.ui.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun InfoPlace (icon: ImageVector, text: String, title: String, color: Color){
    Row (
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
        Column (
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ){
            Text(text=title, style = MaterialTheme.typography.titleMedium)
            Text(text = text, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        }
    }
}