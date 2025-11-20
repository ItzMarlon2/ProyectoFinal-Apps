package com.example.proyectofinal.ui.components

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Place
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions

@Composable
fun Map(
    modifier: Modifier = Modifier,
    places: List<Place> = emptyList(),
    activateClick: Boolean = false,
    onMapClickListener: (Point) -> Unit = {},
    onNavigatePlaceDetail: (String) -> Unit = {}
){
    val context = LocalContext.current
    var clickedPoint by rememberSaveable { mutableStateOf<Point?>(null) }
    val marker = rememberIconImage(
        key = R.drawable.red_marker,
        painter = painterResource( R.drawable.red_marker)
    )
    var mapViewportState = rememberMapViewportState{
        setCameraOptions {
            zoom(4.0)
            center(Point.fromLngLat(-75.6491181, 4.4687891))
            pitch(45.0)
            bearing(0.0)
        }
    }

    val hasPermission = rememberLocationPermissionState{
        Toast.makeText(
            context,
            if(it) "Ha concedido permiso para acceder a su ubicación" else "No ha concedido permiso para ceder a su ubicación",
            Toast.LENGTH_SHORT
        )
    }

    MapboxMap (
        modifier = modifier,
        mapViewportState = mapViewportState,
        onMapClickListener = {
            if(activateClick){
                onMapClickListener(it)
                clickedPoint = it
            }
            true
        }
    ){
        if(hasPermission){
            MapEffect(key1 = "follow_puck") {mapView ->
                mapView.location.updateSettings {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    enabled = true
                    puckBearing= PuckBearing.COURSE
                    puckBearingEnabled = true
                }
            }
            mapViewportState.transitionToFollowPuckState(
                defaultTransitionOptions = DefaultViewportTransitionOptions.Builder().maxDurationMs(0).build()
            )
        }

        clickedPoint?.let{
            PointAnnotation(point=it){
                iconImage = marker
            }
        }

        if(places.isNotEmpty()){
            places.forEach { place ->
                PointAnnotation(
                    point = Point.fromLngLat(place.location.lng, place.location.lat),
                ){
                    iconImage = marker
                    interactionsState.onClicked {
                        onNavigatePlaceDetail(place.id)
                        true
                    }
                }
            }
        }

    }
}

@Composable
fun rememberLocationPermissionState(
    permission: String = android.Manifest.permission.ACCESS_FINE_LOCATION,
    onPermissionResult: (Boolean) -> Unit
): Boolean {
    val context = LocalContext.current
    val permissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
        onPermissionResult(granted)

    }

    LaunchedEffect(Unit) {
        if(!permissionGranted.value){
            launcher.launch(permission)
        }
    }

    return permissionGranted.value


}

@Composable
fun PlaceInfoCard(place: Place, onNavigate: () -> Unit) {
    Card (
        modifier = Modifier
            .width(220.dp) // Ancho fijo para la tarjeta
            .clickable { onNavigate() }, // Hacer toda la tarjeta clicable
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(place.images.firstOrNull()),
                contentDescription = place.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = place.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = place.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2
                )
            }
        }
    }
}