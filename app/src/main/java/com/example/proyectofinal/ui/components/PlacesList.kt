package com.example.proyectofinal.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Place
import com.example.proyectofinal.ui.theme.BorderBoxes
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryLight
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesList(
    padding: PaddingValues,
    onNavigatePlaceDetail: (String) -> Unit,
    places: List<Place>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
){
    val todayName = LocalDate.now().dayOfWeek.getDisplayName(
        TextStyle.FULL,
        Locale("es", "ES")
    ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.padding(padding)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
            if (places.isEmpty() && !isRefreshing) {
                item {
                    Text(
                        text = "No se encontraron lugares. ¡Intenta recargar la lista!",
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(top = 100.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }


            items(places){
                println("info que llega: "+it)

                Column(
                    modifier = Modifier
                        .clickable {
                            onNavigatePlaceDetail(it.id)
                        }
                        .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .fillMaxWidth(),

                    content={
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        ){
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = 0.dp,
                                            bottomEnd = 0.dp
                                        )
                                    )
                                ,
                                model = it.images.firstOrNull() ?: "",
                                contentDescription = it.title,
                                contentScale = ContentScale.Crop
                            )
                            // 2. Buscar el horario del día actual en la lista de schedules
                            val todaySchedule = it.schedules.find { it.day == todayName }

                            // 3. Determinar el texto y el color a mostrar
                            val (statusText, statusColor) = if (todaySchedule?.isOpen == true) {
                                "Abierto" to Color(0xFF388E3C) // Verde oscuro
                            } else {
                                "Cerrado" to Color(0xFFD32F2F) // Rojo oscuro
                            }

                            Text(
                                text = statusText,
                                modifier = Modifier
                                    .align(Alignment.BottomStart) // Posiciona el texto arriba a la derecha
                                    .padding(8.dp)
                                    .background(
                                        statusColor.copy(alpha = 0.8f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold

                            )

                        }
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,

                                ) {
                                Text(it.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Button(
                                    onClick = {
                                        onNavigatePlaceDetail(it.id)
                                    },
                                    colors = ButtonDefaults.buttonColors(Primary),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(10.dp, 4.dp),
                                    content={
                                        Text(
                                            text = stringResource(R.string.btn_ver),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                )
                            }
                            Text(it.description, color = Color.Gray, fontSize = 18.sp)
                            Row(
                                modifier = Modifier.padding(top = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {

                                val totalReviews = it.reviews.size
                                val averageRating = if (totalReviews > 0) {
                                    // Suma todos los ratings y los divide por el total de reseñas
                                    it.reviews.sumOf { it.rating } / totalReviews.toDouble()
                                } else {
                                    0.0 // Si no hay reseñas, el promedio es 0.0
                                }

                                if (totalReviews > 0) {
                                    Row(

                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Star, // Usamos la estrella rellena
                                            contentDescription = "Rating",
                                            tint = Color(0xFFFFC107), // Un color amarillo/dorado típico para estrellas
                                            modifier = Modifier.size(18.dp) // Ajusta el tamaño del ícono
                                        )

                                        // Muestra el promedio formateado a un solo decimal
                                        Text(
                                            text = String.format("%.1f", averageRating),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp,
                                            color = Color.Black
                                        )

                                        // Muestra la cantidad total de reseñas entre paréntesis
                                        Text(
                                            text = "($totalReviews)",
                                            color = Color.Gray,
                                            fontSize = 17.sp
                                        )
                                    }
                                }else{
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFC107),
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Text(
                                        text = "0",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "(0)",
                                        color = Color.Gray,
                                        fontSize = 17.sp
                                    )
                                }
                                Row (
                                    modifier = Modifier.padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Icon(imageVector = Icons.Outlined.PinDrop, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                                    Text(text = it.address, color = Color.Gray, fontSize = 18.sp)
                                }
                            }
                            Row(
                                modifier = Modifier.padding(top = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 2. Buscar el horario del día actual en la lista de schedules
                                val todaySchedule = it.schedules.find { it.day == todayName }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                                    Text(text = " ${todaySchedule?.openTime} -", color = Color.Gray, fontSize = 18.sp)
                                }
                                Text(text = "${todaySchedule?.closeTime}", color = Color.Gray, fontSize = 18.sp)

                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,

                            ){
                                if (it.creationDate != null) {
                                    println("creado: "+ it.creationDate)
                                    // 1. Convierte el Timestamp a un objeto Date de Java
                                    val dateObject = it.creationDate.toDate()

                                    // 2. Define el formato que QUIERES (ej. "dd/MM/yyyy")
                                    val formatter = SimpleDateFormat("dd 'de' MMM, yyyy", Locale("es", "ES"))

                                    // 3. Formatea el objeto Date a un String con ese formato
                                    val formattedDate = formatter.format(dateObject) // Esto dará "18/11/2025"

                                    Row(
                                        modifier = Modifier.padding(top = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Agregado el:",
                                            color = Color.Gray,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = formattedDate,
                                            color = Color.Gray,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Icon(imageVector = Icons.Outlined.Favorite, contentDescription = null, tint = Color.Red)

                            }

                        }
                    },
                )
//            ListItem(
//                modifier = Modifier
//                    .clip(MaterialTheme.shapes.large)
//                    .clickable{
//
//                    },
//                headlineContent = { Text(it.title) },
//                supportingContent = { Text(it.description) },
//                leadingContent = {
//                    AsyncImage(
//                    modifier = Modifier
//                        .width(120.dp)
//                        .height(120.dp)
//                        .clip(RoundedCornerShape(
//                            topStart = 16.dp,
//                            topEnd = 16.dp,
//                            bottomStart = 0.dp,
//                            bottomEnd = 0.dp
//                        ))
//                        ,
//                    model = it.images[0],
//                    contentDescription = it.title,
//                    contentScale = ContentScale.Crop
//                )},
//            )

            }
        }
    }

}