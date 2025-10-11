package com.example.proyectofinal.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryLight

@Composable
fun PlacesLits(padding: PaddingValues, onNavigatePlaceDetail: (String) -> Unit, places: List<Place>){
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(padding)
    ) {

        items(places){
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .clickable{
                        onNavigatePlaceDetail(it.id)
                    }
                    .border(1.dp, PrimaryLight, MaterialTheme.shapes.large)
                    .fillMaxWidth(),

                content={
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            ))
                        ,
                        model = it.images[0],
                        contentDescription = it.title,
                        contentScale = ContentScale.Crop
                    )
                    Column (
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                        Text(it.description, color = Color.Gray, fontSize = 15.sp)

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