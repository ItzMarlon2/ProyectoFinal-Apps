package com.example.proyectofinal.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.model.AlertType
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryLight
import com.example.proyectofinal.ui.theme.PrimaryUltraLight

@Composable
fun AlertMessage (
    modifier: Modifier = Modifier,
    type: AlertType,
    message: String
){

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(type == AlertType.ERROR) Color(0xFFFFCDD2) else PrimaryUltraLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
           modifier = Modifier
               .padding(16.dp)
               .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if(type == AlertType.ERROR) Icons.Rounded.Error else Icons.Rounded.CheckCircleOutline,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if(type == AlertType.ERROR) Color(0xFFD32F2F) else Primary

            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text=message,
                color = if(type == AlertType.ERROR) Color(0xFFD32F2F) else Primary

            )


        }

    }
}