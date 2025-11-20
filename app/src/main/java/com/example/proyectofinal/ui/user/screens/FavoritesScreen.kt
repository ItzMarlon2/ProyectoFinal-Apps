package com.example.proyectofinal.ui.user.screens
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.components.PlacesList
import com.example.proyectofinal.ui.navigation.localMainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Favorites(padding: PaddingValues, onNavigatePlaceDetail: (String) -> Unit, userId:String?){
    val mainViewModel = localMainViewModel.current
    val placesViewModel = mainViewModel.placesViewModel

    val allPlaces by placesViewModel.places.collectAsState()

    val favoritePlaces = allPlaces.filter { place ->
        userId?.let { userId ->
            place.favoritedBy.contains(userId)
        } ?: false
    }
    val isRefreshing by placesViewModel.isRefreshing.collectAsState()


    LaunchedEffect(Unit) {
        placesViewModel.getAll()
    }

    Column(modifier = Modifier.padding(padding)) {
        if (favoritePlaces.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.aun_no_hay_reseña), style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            }
        } else {
            PlacesList(
                places = favoritePlaces,
                padding = padding,
                onNavigatePlaceDetail = onNavigatePlaceDetail,
                isRefreshing  = isRefreshing,
                onRefresh = {placesViewModel.getAll()}
            )
        }
    }
}