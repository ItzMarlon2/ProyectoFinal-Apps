package com.example.proyectofinal.ui.places

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.BlurCircular
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.proyectofinal.model.Place
import com.example.proyectofinal.ui.components.InfoPlace
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.ui.theme.BorderBoxes
import com.example.proyectofinal.ui.theme.Primary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetail (id: String, onNavigateBack: () -> Unit){
    val placesViewModel = localMainViewModel.current.placesViewModel
    val place = placesViewModel.findById(id)
    var value by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "",

                    )
                },
                navigationIcon = {
                    IconButton (onClick = { onNavigateBack()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = "Download"
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Download"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (place != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ){
                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item{PlaceImageCarousel(place = place)}
                    item{
                        Column (
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.padding(16.dp)
                        ){
                            Text(text = place.title, style=MaterialTheme.typography.headlineMedium)
                            Text(text = place.type.name, style=MaterialTheme.typography.labelMedium)
                            Text(text = place.description, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                            Column(
                                modifier = Modifier
                                    .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp)).background(Color.White).padding(20.dp).fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                InfoPlace(Icons.Outlined.LocationOn, place.address, "Dirección", Color.Red)
                                InfoPlace(Icons.Outlined.AccessTime, "7:00 AM - 7:00 PM", "Horario", Color.Green)
                                InfoPlace(Icons.Outlined.Call, "315343134", "Teléfono", Color.Yellow)
                                InfoPlace(Icons.Outlined.BlurCircular, "www.example.com", "Sitio web",
                                    Primary
                                )

                            }
                            Column(
                                modifier = Modifier
                                    .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp)).background(Color.White).padding(20.dp).fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ){
                                Text(text = "Deja tu reseña", style = MaterialTheme.typography.titleLarge)
                                Text(text = "Tu calificación", style = MaterialTheme.typography.bodyLarge)
                                Row (
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Outlined.StarOutline, contentDescription = null)
                                    Icon(imageVector = Icons.Outlined.StarOutline, contentDescription = null)
                                    Icon(imageVector = Icons.Outlined.StarOutline, contentDescription = null)
                                    Icon(imageVector = Icons.Outlined.StarOutline, contentDescription = null)
                                    Icon(imageVector = Icons.Outlined.StarOutline, contentDescription = null)
                                }
                                OutlinedTextField(
                                    value = value,
                                    onValueChange = {value = it},
                                    label = { Text("Deja tu reseña") },
                                    singleLine = false,
                                    minLines = 3,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                                        disabledContainerColor = Color.LightGray.copy(alpha = 0.4f),
                                        focusedBorderColor = Primary.copy(alpha = 0.4f),
                                        unfocusedBorderColor = Color.Transparent,
                                        disabledBorderColor = Color.Transparent
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),

                                )
                                Button(
                                    onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(Primary, ),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                        Row(
                                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                                        ) {
                                            Icon(imageVector = Icons.Outlined.Send, contentDescription = null, tint = Color.White)
                                            Text(text = "Publicar reseña", style = MaterialTheme.typography.titleMedium)
                                        }
                                }

                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Lugar no encontrado")
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceImageCarousel(place: Place) {

    val images = place.images

    if (images.isEmpty()) return

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { images.count() },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp),
        preferredItemWidth = 186.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { i ->
        val imageUrl = images[i]
        AsyncImage(
            model = imageUrl,
            modifier = Modifier
                .height(300.dp)
                .maskClip(MaterialTheme.shapes.extraLarge),
            contentDescription = place.title,
            contentScale = ContentScale.Crop
        )
    }
}