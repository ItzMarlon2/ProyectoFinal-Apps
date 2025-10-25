package com.example.proyectofinal.ui.places

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceScreen(userId: String?, onNavigateBack: () -> Unit){
    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler(
        enabled = !showExitDialog
    ) {
        showExitDialog = true
    }
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Crear Lugar")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            showExitDialog =true
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ){
        Box(
            modifier = Modifier.padding(it)
        )
    }

    if(showExitDialog){
        AlertDialog(
            title = {
                 Text(text="Está seguro de salir?")
            },
            text = {
                Text(text = "Si sale perderá los cambios")
            },
            onDismissRequest = {
                showExitDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text(text = "Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                    }
                ) {
                    Text(text = "Cerrar")
                }
            }
        )
    }
}