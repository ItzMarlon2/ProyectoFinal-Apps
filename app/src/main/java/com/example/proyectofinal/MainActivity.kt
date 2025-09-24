package com.example.proyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.proyectofinal.ui.screens.Navigation
import com.example.proyectofinal.ui.theme.BackgroundColorFromHSL
import com.example.proyectofinal.ui.theme.MutedColorFromHSL
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.ProyectoFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gradientColors = listOf(
            BackgroundColorFromHSL,
            MutedColorFromHSL.copy(alpha = 0.3f),
            Primary.copy(alpha = 0.3f)
        )
        setContent (

            content ={
                ProyectoFinalTheme {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = gradientColors,
                                    start = Offset.Zero,
                                    end = Offset.Infinite
                                )
                            ),
                        contentAlignment = Alignment.Center,
                        content = {
                            Navigation()
                        }
                    )
                }

            }
            )
    }
}

