package com.example.proyectofinal.ui.user.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.components.PlacesList
import com.example.proyectofinal.ui.navigation.localMainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    padding: PaddingValues,
    onNavigatePlaceDetail: (String) -> Unit
){
    val placesViewModel = localMainViewModel.current.placesViewModel
    var query by rememberSaveable { mutableStateOf("") }
    var expanded  by rememberSaveable { mutableStateOf(false) }
    val isRefreshing by placesViewModel.isRefreshing.collectAsState()

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        query = it
                    },
                    onSearch = {
                        expanded=false
                    },
                    expanded = expanded,
                    onExpandedChange = {expanded = it},
                    placeholder = {Text("Search")}
                )
            },
            expanded = expanded,
            onExpandedChange = {expanded = it}

        ){
            if(query.isNotEmpty()){
                val places = placesViewModel.findByName(query)

                LazyColumn {
                    items(places){
                        Text(modifier = Modifier.padding(5.dp).clickable{
                            query = it.title
                            expanded=false
                        },text= it.title)
                        HorizontalDivider()
                    }
                }
            }
        }
        if(query.isNotEmpty()){
            val places = placesViewModel.findByName(query)
            PlacesList(
                places = places,
                padding = padding,
                onNavigatePlaceDetail = onNavigatePlaceDetail,
                isRefreshing = isRefreshing,
                onRefresh = {placesViewModel.getAll()}
            )
        }else{
            Box(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = stringResource(R.string.no_hay_resultados))

            }

        }
    }
}