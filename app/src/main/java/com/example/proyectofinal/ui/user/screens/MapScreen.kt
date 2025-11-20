package com.example.proyectofinal.ui.user.screens

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.components.Map
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.ui.components.myPlacesList
import com.example.proyectofinal.ui.theme.Primary

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Map(padding: PaddingValues, onNavigatePlaceDetail: (String) -> Unit){

    val placesViewModel = localMainViewModel.current.placesViewModel
    val places by placesViewModel.places.collectAsState()
    val isRefreshing by placesViewModel.isRefreshing.collectAsState()
    val isInitialLoading = places.isEmpty() && isRefreshing

    LaunchedEffect(Unit) {
        if (places.isEmpty()) {
            placesViewModel.getAll()
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isInitialLoading) {
            CircularProgressIndicator(
                color = Primary
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Map(
                    places = places,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    onNavigatePlaceDetail = onNavigatePlaceDetail
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.lugares_cerca), style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "${places.size} ${stringResource(R.string.lugares)}",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    myPlacesList(
                        places = places,
                        onNavigatePlaceDetail = onNavigatePlaceDetail,
                        isRefreshing = isRefreshing,
                        onRefresh = { placesViewModel.getAll() },
                    )
                }
            }
        }
    }
}

