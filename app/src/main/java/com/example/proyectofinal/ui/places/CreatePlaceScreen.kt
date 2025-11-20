package com.example.proyectofinal.ui.places

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.proyectofinal.R
import com.example.proyectofinal.model.City
import com.example.proyectofinal.model.Location
import com.example.proyectofinal.model.Place
import com.example.proyectofinal.model.PlaceType
import com.example.proyectofinal.model.Schedule
import com.example.proyectofinal.ui.components.DropDownMenu
import com.example.proyectofinal.ui.components.InputText
import com.example.proyectofinal.ui.components.Map
import com.example.proyectofinal.ui.components.OperationResultHandler
import com.example.proyectofinal.ui.components.ScheduleDayRow
import com.example.proyectofinal.ui.navigation.localMainViewModel
import com.example.proyectofinal.ui.places.state.PlaceFormData
import com.example.proyectofinal.ui.places.state.PlaceFormDataSaver
import com.example.proyectofinal.ui.theme.BackgroundPrimaryColor
import com.example.proyectofinal.ui.theme.BorderBoxes
import com.example.proyectofinal.ui.theme.Primary
import com.example.proyectofinal.ui.theme.PrimaryLight
import com.example.proyectofinal.ui.theme.PrimaryUltraLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.apply
import kotlin.collections.firstOrNull
import kotlin.concurrent.schedule

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaceScreen(userId: String?, onNavigateBack: () -> Unit){

    val placeViewModel = localMainViewModel.current.placesViewModel
    val placeResult by placeViewModel.placeResult.collectAsState()
    var formData by rememberSaveable(stateSaver = PlaceFormDataSaver) { mutableStateOf(PlaceFormData()) }
    val onFormChange: (PlaceFormData) -> Unit = { newFormData ->
        formData = newFormData
    }

    var showExitDialog by remember { mutableStateOf(false) }
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Información", "Ubicación", "Fotos", "Mapa")
    val context = LocalContext.current



    BackHandler(
        enabled = !showExitDialog
    ) {
        showExitDialog = true
    }
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.crear_lugar))
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
    ){ paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally

        ){

            PrimaryTabRow(
                selectedTabIndex = tabIndex,
                divider = {},
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(selectedTabIndex = tabIndex)
                            .height(10.dp)
                            .padding(horizontal = 32.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Primary
                    )
                }

            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = {  },
                        unselectedContentColor = PrimaryLight
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                when (tabIndex){
                    0 -> FormSectionInfo(formData, onFormChange)
                    1 -> FormSectionLocation(formData, onFormChange)
                    2 -> FormSectionPhotos(formData, onFormChange, context)
                    3 -> FormSectionMap(formData, onFormChange)
                }
            }
            OperationResultHandler(
                result = placeResult,
                onSuccess = {
                    onNavigateBack()
                    placeViewModel.resetOperationResult()
                },
                onFailure = {
                    placeViewModel.resetOperationResult()
                }
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                if(tabIndex > 0){
                    Button(
                        modifier = Modifier.weight(.5f),
                        onClick = {tabIndex--},
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Black)

                        ) {
                        Text(text = stringResource(R.string.anterior))
                    }
                }else{
                }

                Button(
                    onClick = {
                        if(tabIndex < tabs.size - 1){
                            tabIndex++
                        }else{
                            val place = Place(
                                id="",
                                title = formData.name,
                                description = formData.description,
                                type = formData.category,
                                location = Location(formData.location!!.latitude(), formData.location!!.longitude()),
                                phones = listOf(formData.phone),
                                images = formData.images,
                                schedules = formData.schedule,
                                ownerId = userId ?: "",
                                website = formData.website,
                                city = formData.city,
                                address = formData.address,
                            )
                            placeViewModel.create(place)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Primary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .then(
                            if (tabIndex > 0) {
                                Modifier.weight(.5f)

                            } else {
                                Modifier.fillMaxWidth()

                            }
                        ),
                    enabled = if(tabIndex == 0 ){
                        formData.name.isNotBlank() && formData.description.isNotBlank() && formData.category != null && formData.address.isNotBlank() && formData.city != null
                    }else if(tabIndex == 2){
                        formData.images.isNotEmpty()
                    }else if(tabIndex == 3){
                        formData.location != null
                    }else{
                        true
                    }


                ) {
                    Text(if (tabIndex < tabs.size - 1) stringResource(R.string.siguiente) else stringResource(R.string.crear_lugar), style =MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
                }

            }

        }
    }

    if(showExitDialog){
        AlertDialog(
            title = {
                 Text(text=stringResource(R.string.esta_seguro))
            },
            text = {
                Text(text = stringResource(R.string.si_sale_perdera))
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
                    Text(text = stringResource(R.string.confirmar))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.cerrar))
                }
            }
        )
    }
}

@Composable
fun FormSectionInfo(
    formData: PlaceFormData,
    onFormChange: (PlaceFormData) -> Unit
) {
    val (isErrorNamePlace, setIsErrorNamePlace) = rememberSaveable { mutableStateOf(false) }
    val (isErrorDescription, setIsErrorDescription) = rememberSaveable { mutableStateOf(false)}
    val (isErrorAddress, setIsErrorAddress) = rememberSaveable { mutableStateOf(false)}
    val categoryOptions = remember { PlaceType.entries.map {it.displayName} }
    val cityOptions = remember { City.entries.map {it.displayName} }


    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(25.dp)
            .fillMaxWidth()
    ) {
        item {  Text(stringResource(R.string.info_basica), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))}
        item {
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ){

                InputText(
                    value = formData.name,
                    setValue = { newName ->
                        onFormChange(formData.copy(name = newName))
                    },
                    text = stringResource(R.string.form_label_place_name),
                    place = stringResource(R.string.form_placeholder_place_name),
                    textError = stringResource(R.string.form_error_field_cannot_be_empty),
                    isError = isErrorNamePlace,
                    setError = setIsErrorNamePlace,
                    onValidate = {
                        formData.name.isBlank()
                    }
                )
                DropDownMenu(
                    items = categoryOptions,
                    selectedItem = formData.category?.displayName ?: stringResource(R.string.form_placeholder_select_category),
                    text = stringResource(R.string.form_label_category),
                    onItemSelected = { selectedName ->
                        val newCategory = PlaceType.entries.find { it.displayName == selectedName }
                        onFormChange(formData.copy(category = newCategory))
                    }
                )
                InputText(
                    value = formData.description,
                    setValue = { newDescription ->
                        onFormChange(formData.copy(description = newDescription))
                    },
                    text = stringResource(R.string.form_label_description),
                    place = stringResource(R.string.form_placeholder_description),
                    textError = stringResource(R.string.form_error_field_is_required),
                    isError = isErrorDescription,
                    setError = setIsErrorDescription,
                    onValidate = {
                        formData.description.isBlank()
                    },
                    multiline = true
                )
                InputText(
                    value = formData.address,
                    setValue = { newAddress ->
                        onFormChange(formData.copy(address = newAddress))
                    },
                    text = stringResource(R.string.form_label_address),
                    place = stringResource(R.string.form_placeholder_address),
                    textError = stringResource(R.string.form_error_field_is_required), // Reutilizando el string de error
                    isError = isErrorAddress,
                    setError = setIsErrorAddress,
                    onValidate = {
                        formData.address.isBlank()
                    },
                    multiline = true
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(space = 15.dp),
                ) {
                    Text(
                        text = stringResource(R.string.form_label_city),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    DropDownMenu(
                        items = cityOptions,
                        selectedItem = formData.city?.displayName ?: stringResource(R.string.form_placeholder_select_city),
                        text = "",
                        onItemSelected = { city ->
                            val newCity = City.entries.find { it.displayName == city }
                            onFormChange(formData.copy(city = newCity))
                        },
                    )
                }


            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormSectionLocation(
    formData: PlaceFormData,
    onFormChange: (PlaceFormData) -> Unit
) {
    val (isErrorPhone, setIsErrorPhone) = rememberSaveable { mutableStateOf(false) }
    val (isErrorUrl, setIsErrorUrl) = rememberSaveable { mutableStateOf(false) }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    LaunchedEffect(Unit) {
        if (formData.schedule.isEmpty()) {
            val weekDays = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
            val initialSchedule = weekDays.map { day ->
                Schedule(
                    day = day,
                    isOpen = true,
                    openTime = "09:00",
                    closeTime = "17:00"
                )
            }
            onFormChange(formData.copy(schedule = initialSchedule))
        }
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        item {
            Column (
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier
                    .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(25.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.info_contacto), style = MaterialTheme.typography.titleLarge)
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    InputText(
                        value = formData.phone,
                        setValue = {newPhone ->
                            onFormChange(formData.copy(phone = newPhone))
                        },
                        text = stringResource(R.string.form_label_phone),
                        place = stringResource(R.string.form_placeholder_phone),
                        textError = stringResource(R.string.form_error_invalid_phone),
                        isError = isErrorPhone,
                        setError = setIsErrorPhone,
                        onValidate = {
                            formData.phone.isBlank() || !Patterns.PHONE.matcher(formData.phone).matches()
                        },
                        icon = Icons.Outlined.Phone
                    )
                    InputText(
                        value = formData.website,
                        setValue = {newWebsite ->
                            onFormChange(formData.copy(website = newWebsite))
                        },
                        text = stringResource(R.string.form_label_website),
                        place = stringResource(R.string.form_placeholder_website),
                        textError = stringResource(R.string.form_error_invalid_url),
                        isError = isErrorUrl,
                        setError = setIsErrorUrl,
                        onValidate = {
                            formData.website.isBlank() || !Patterns.WEB_URL.matcher(formData.website).matches()
                        },
                        icon = Icons.Outlined.Public
                    )

                }

            }
        }
        item {
            Column (
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier
                    .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(25.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Outlined.AccessTime, contentDescription = null, tint = Color.Black)
                    Text(stringResource(R.string.form_label_schedule), style = MaterialTheme.typography.titleLarge)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    formData.schedule.forEachIndexed { index, daySchedule ->
                        ScheduleDayRow(
                            daySchedule = daySchedule,
                            onDayChange = { updatedDaySchedule ->
                                val newScheduleList = formData.schedule.toMutableList().apply {
                                    this[index] = updatedDaySchedule
                                }
                                onFormChange(formData.copy(schedule = newScheduleList))
                            }
                        )
                    }
                }

            }
        }
    }


}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormSectionPhotos(
    formData: PlaceFormData,
    onFormChange: (PlaceFormData) -> Unit,
    context: Context
) {
    val config = mapOf(
        "cloud_name" to "nextjs-crud",
        "api_key" to "795866198588463",
        "api_secret" to "8vCciAeu2xP0O-eenx5kLN6XujM"
    )

    val scope = rememberCoroutineScope()
    val cloudinary = Cloudinary(config)

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch(Dispatchers.IO){
                val inputStream = context.contentResolver.openInputStream(it)
                inputStream?.use {stream ->
                    val result = cloudinary.uploader().upload(stream, ObjectUtils.emptyMap())
                    val imageUrl = result["secure_url"].toString()
                    val currentImages = formData.images
                    val updatedImages = currentImages + imageUrl
                    onFormChange(formData.copy(images = updatedImages))

                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){
        if(it){
            Toast.makeText(context, "Permiso concedido", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),

    ) {
        Text(stringResource(R.string.imagenes), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.agrega_fotos), color = Color.Gray)

        Column (
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(25.dp)
                .fillMaxWidth()
        ){
            Button(
                colors = ButtonDefaults.buttonColors(BackgroundPrimaryColor, Color.Black),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp)),
                onClick = {
                    val permissionCheckResult = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                    }else{
                        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    if(permissionCheckResult == PackageManager.PERMISSION_GRANTED){
                        fileLauncher.launch("image/*")
                    }else{
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }else{
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                },
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                    Text(stringResource(R.string.agregar), style = MaterialTheme.typography.titleSmall)
                }
            }
            val images = formData.images
            if (images.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    images.forEach { imageUrl ->
                        ImagePreviewItem(
                            imageUrl = imageUrl,
                            onRemoveClick = {
                                val updatedImages = formData.images - imageUrl
                                onFormChange(formData.copy(images = updatedImages))
                            }
                        )
                    }
                }

            }

        }
    }
}

@Composable
fun FormSectionMap(
    formData: PlaceFormData,
    onFormChange: (PlaceFormData) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),

        ) {
        Text(stringResource(R.string.ubicacion_en_el_mapa), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.elige_la_ubicacion), color = Color.Gray)

        Column (
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp))
                .background(Color.White)
                .fillMaxWidth()
        ){
            Map(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, BorderBoxes, RoundedCornerShape(8.dp)),
                activateClick = true,
                onMapClickListener = { point->
                    onFormChange(formData.copy(location = point))
                }
            )
        }
    }
}

@Composable
fun ImagePreviewItem(
    imageUrl: String,
    onRemoveClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.imagen_subida),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        )

        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier
                .padding(2.dp)
                .size(18.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.eliminar_imagen),
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


