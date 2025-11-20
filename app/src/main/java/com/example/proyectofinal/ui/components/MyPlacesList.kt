package com.example.proyectofinal.ui.components


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
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
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
fun LazyListScope.myPlacesList(
    places: List<Place>,
    onNavigatePlaceDetail: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
){
    val todayName = LocalDate.now().dayOfWeek.getDisplayName(
        TextStyle.FULL,
        Locale("es", "ES")
    ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


    items(
            places,
        ){
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .clickable {
                            onNavigatePlaceDetail(it.id)
                        }
                        .border(1.dp, BorderBoxes, MaterialTheme.shapes.large),


                    content = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .weight(.3f)
                                    .height(150.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            bottomStart = 16.dp,
                                            topEnd = 0.dp,
                                            bottomEnd = 0.dp
                                        )
                                    ),
                                model = it.images.firstOrNull() ?: "",
                                contentDescription = it.title,
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier.weight(.7f).padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,

                                    ) {
                                    Text(it.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowForwardIos,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(12.dp)
                                    )

                                }
                                Text(it.type!!.displayName, color = Color.Gray, fontSize = 15.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,

                                    ) {
                                    val totalReviews = it.reviews.size
                                    val averageRating = if (totalReviews > 0) {
                                        it.reviews.sumOf { it.rating } / totalReviews.toDouble()
                                    } else {
                                        0.0
                                    }

                                    Row(

                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFC107),
                                            modifier = Modifier.size(18.dp)
                                        )


                                        Text(
                                            text = String.format("%.1f", averageRating),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.Black
                                        )

                                        Text(
                                            text = "($totalReviews ${stringResource(R.string.reseñas)})",
                                            color = Color.Gray,
                                            fontSize = 15.sp
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Filled.Circle,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(5.dp)
                                    )
                                    Text(
                                        it.city!!.displayName,
                                        color = Color.Gray,
                                        fontSize = 15.sp
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,


                                    ) {
                                    val todaySchedule = it.schedules.find { it.day == todayName }
                                    val (statusText, statusColor) = if (todaySchedule?.isOpen == true) {
                                        stringResource(R.string.abierto) to Color(0xFF388E3C)
                                    } else {
                                        stringResource(R.string.cerrado) to Color(0xFFD32F2F)
                                    }
                                    Text(
                                        text = " ${todaySchedule?.openTime} - ${todaySchedule?.closeTime}",
                                        color = Color.Gray,
                                        fontSize = 15.sp
                                    )


                                    Text(
                                        text = statusText,
                                        color = statusColor.copy(alpha = 0.8f),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                }

                            }
                        }
                    },
                )
            }

        }
    }
