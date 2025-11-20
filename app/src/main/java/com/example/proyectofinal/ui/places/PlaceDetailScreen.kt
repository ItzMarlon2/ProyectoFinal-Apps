package com.example.proyectofinal.ui.places

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BlurCircular
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.proyectofinal.R
import com.example.proyectofinal.model.Place
import com.example.proyectofinal.model.Review
import com.example.proyectofinal.ui.components.InfoPlace
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.ui.theme.BorderBoxes
import com.example.proyectofinal.ui.theme.Primary
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetail (placeId: String, userName: String?, userId:String?, onNavigateBack: () -> Unit){
    val placesViewModel = localMainViewModel.current.placesViewModel
    val reviewsViewModel = localMainViewModel.current.reviewsViewModel
    val allPlaces by placesViewModel.places.collectAsState()

    val place = remember(allPlaces, placeId) {
        allPlaces.find { it.id == placeId }
    }

    val reviews by reviewsViewModel.reviews.collectAsState()

    var value by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showComments by remember { mutableStateOf(false) }

    LaunchedEffect(placeId) {
        reviewsViewModel.getReviewsByPlaceId(placeId)
    }
    val todayName = LocalDate.now().dayOfWeek.getDisplayName(
        java.time.format.TextStyle.FULL,
        Locale("es", "ES")
    ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


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
                    IconButton(onClick = {
                        if (place != null) {
                            placesViewModel.toggleFavorite(placeId, userId ?: "")
                        }
                    }) {
                        val isFavorite = place!!.favoritedBy.contains(userId)

                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) Color.Red else Color.Gray,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showComments = true
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Default.Comment, contentDescription = null)
            }
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(text = place.title, style=MaterialTheme.typography.headlineMedium)
                                val todaySchedule = place.schedules.find { it.day == todayName }

                                val (statusText, statusColor) = if (todaySchedule?.isOpen == true) {
                                    "Abierto" to Color(0xFF388E3C)
                                } else {
                                    "Cerrado" to Color(0xFFD32F2F)
                                }

                                Text(
                                    text = statusText,
                                    modifier = Modifier
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
                            val totalReviews = place.reviews.size
                            val averageRating = if (totalReviews > 0) {
                                place.reviews.sumOf { it.rating } / totalReviews.toDouble()
                            } else {
                                0.0
                            }

                                Row(

                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row {

                                        repeat(5) { starIndex ->
                                            val isFilled = starIndex < averageRating
                                            Icon(
                                                imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.Star,
                                                contentDescription = null,
                                                tint = if (isFilled) Color(0xFFFFC107) else Color.Gray,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = String.format("%.1f", averageRating),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "($totalReviews reseñas)",
                                        color = Color.Gray,
                                        fontSize = 17.sp
                                    )
                                }

                            Text(modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    Color.White,
                                    RoundedCornerShape(20.dp)
                                ),
                                text = place.type!!.displayName,
                                style=MaterialTheme.typography.labelMedium
                            )
                            ExpandableText(
                                text = place.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                            Column(
                                modifier = Modifier
                                    .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(20.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                InfoPlace(Icons.Outlined.LocationOn, place.address+" - "+place.city!!.displayName,
                                    stringResource(R.string.direccion), Color.Red)
                                val todaySchedule = place.schedules.find { it.day == todayName }

                                InfoPlace(Icons.Outlined.AccessTime, todaySchedule?.openTime+" - "+todaySchedule?.closeTime, stringResource(R.string.horario), Color.Green)
                                InfoPlace(Icons.Outlined.Call, place.phones.firstOrNull() ?: stringResource(R.string.no_disponible), stringResource(R.string.form_label_phone), Color.Yellow)
                                InfoPlace(Icons.Outlined.BlurCircular, place.address, stringResource(R.string.form_label_website),
                                    Primary
                                )

                            }
                            Column(
                                modifier = Modifier
                                    .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(20.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ){
                                var rating by remember { mutableStateOf(0) }
                                var comment by remember { mutableStateOf("") }
                                val userName = userName ?: stringResource(R.string.usuario_anonimo)

                                Text(text = stringResource(R.string.deja_reseña), style = MaterialTheme.typography.titleLarge)
                                Text(text = stringResource(R.string.tu_calificacion), style = MaterialTheme.typography.bodyLarge)
                                StarRatingInput(
                                    rating = rating,
                                    onRatingChange = { rating = it }
                                )
                                OutlinedTextField(
                                    value = comment,
                                    onValueChange = {comment = it},
                                    label = { Text(stringResource(R.string.deja_reseña)) },
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
                                    onClick = {
                                        if (comment.isNotBlank() && rating > 0 && userId != null && place != null) {
                                            val newReview = Review(
                                                userId = userId,
                                                userName = userName,
                                                placeId = place.id,
                                                rating = rating,
                                                comment = comment
                                            )

                                            reviewsViewModel.createReview(newReview) { createdReview ->
                                                reviewsViewModel.getReviewsByPlaceId(place.id)
                                                comment = ""
                                                rating = 0
                                            }
                                        }
                                    },
                                    enabled = comment.isNotBlank() && rating > 0 && userId != null,
                                    colors = ButtonDefaults.buttonColors(Primary),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                        Row(
                                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                                        ) {
                                            Icon(imageVector = Icons.Outlined.Send, contentDescription = null, tint = Color.White)
                                            Text(text = stringResource(R.string.publicar_reseña), style = MaterialTheme.typography.titleMedium)
                                        }
                                }

                            }
                        }
                    }
                }
            }
            if (showComments) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        showComments = false
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.reseñas),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }

                        if (reviews.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(stringResource(R.string.aun_no_hay_reseña), color = Color.Gray)
                                }
                            }
                        } else {
                            items(reviews) { review ->
                                ReviewsLists(review = review)
                            }
                        }

                        item {
                            CreateReview(
                                placeId = placeId,
                                userId = userId,
                                onCreateReview = { comment, rating ->
                                    val newReview = Review(
                                        userId = userId ?: "",
                                        userName = userName!!,
                                        placeId = place.id,
                                        rating = rating,
                                        comment = comment
                                    )
                                    reviewsViewModel.createReview(newReview) {
                                        reviewsViewModel.getReviewsByPlaceId(place.id)
                                    }
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }

        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.lugar_no_encontrado))
            }
        }

    }
}

@Composable
fun ReviewsLists(review: Review) {
    ListItem(
        headlineContent = { Text(text = review.userName) },
        supportingContent = { Text(text = review.comment) },
        leadingContent = { Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = null) },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = review.rating.toString(), style = MaterialTheme.typography.bodyMedium)
                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFC107))
            }
        }
    )
    HorizontalDivider()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateReview (
    placeId: String,
    userId: String?,
    onCreateReview: (String, Int) -> Unit
){
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    val isButtonEnabled = comment.isNotBlank() && rating > 0 && userId != null


    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("${stringResource(R.string.tu_calificacion)}:", style = MaterialTheme.typography.bodyMedium)
        StarRatingInput(
            rating = rating,
            onRatingChange = { rating = it }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(text = stringResource(R.string.deja_reseña))
                },
                colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                focusedBorderColor = Primary.copy(alpha = 0.4f),
                unfocusedBorderColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),

            )
            IconButton(
                onClick = {
                    onCreateReview(comment, rating)
                    comment = ""
                    rating = 0
                },
                enabled = isButtonEnabled

            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = stringResource(R.string.enviar_reseña),
                    tint = if (isButtonEnabled) Primary else Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceImageCarousel(place: Place) {

    val images = place.images
    if (images.isEmpty()) return
    if(images.size == 1){
        AsyncImage(
            model = images[0],
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentDescription = place.title,
            contentScale = ContentScale.Crop
        )
    }else{
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

}

@Composable
fun StarRatingInput(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    maxRating: Int = 5
) {
    Row {
        for (i in 1..maxRating) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarOutline
            val color = if (isSelected) Color(0xFFFFC107) else Color.Gray

            IconButton(onClick = { onRatingChange(i) }) {
                Icon(
                    imageVector = icon,
                    contentDescription = "${stringResource(R.string.rating)} $i",
                    tint = color
                )
            }
        }
    }
}

@Composable
fun ExpandableText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = Color.Gray,
    maxLinesCollapsed: Int = 4
) {
    var isExpanded by remember { mutableStateOf(false) }

    var isOverflowing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.animateContentSize()
    ) {
        Text(
            text = text,
            style = style,
            color = color,

            maxLines = if (isExpanded) Int.MAX_VALUE else maxLinesCollapsed,

            onTextLayout = { textLayoutResult ->
                isOverflowing = textLayoutResult.hasVisualOverflow
            }
        )

        if (isOverflowing && !isExpanded) {
            Text(
                text = stringResource(R.string.ver_mas),
                style = MaterialTheme.typography.bodyLarge,
                color = Primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { isExpanded = true }
                    .padding(top = 4.dp)
            )
        }
    }
}
