package com.example.proyectofinal.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.model.City
import com.example.proyectofinal.model.Location
import com.example.proyectofinal.model.Place
import com.example.proyectofinal.model.PlaceType
import com.example.proyectofinal.model.Review
import com.example.proyectofinal.model.Schedule
import com.example.proyectofinal.model.User
import com.example.proyectofinal.utils.RequestResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

class PlacesViewModel: ViewModel() {
    private val _places= MutableStateFlow(emptyList<Place>())
    val places: StateFlow<List<Place>> = _places.asStateFlow()
    private val _myPlaces= MutableStateFlow(emptyList<Place>())
    val myPlaces: StateFlow<List<Place>> = _myPlaces.asStateFlow()
    private val _placeResult = MutableStateFlow<RequestResult?>(null)
    val placeResult: StateFlow<RequestResult?> = _placeResult.asStateFlow()

    private val _currentPlace = MutableStateFlow<Place?>(null)
    val currentPlace: StateFlow<Place?> = _currentPlace.asStateFlow()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()


    val db = Firebase.firestore


    init{
        getAll()
    }

    private suspend fun loadPlacesWithReviews(): List<Place> {
        val placesList = getAllFirebase()

        // 2. Obtenemos todas las reseñas en una sola consulta
        val reviewsSnapshot = db.collection("reviews")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
        val allReviews = reviewsSnapshot.mapNotNull { it.toObject(Review::class.java) }

        // 3. Agrupamos las reseñas por su 'placeId' para buscarlas fácilmente
        val reviewsByPlaceId = allReviews.groupBy { it.placeId }

        // 4. Asignamos a cada lugar la lista de reseñas que le corresponde
        placesList.forEach { place ->
            place.reviews = reviewsByPlaceId[place.id] ?: emptyList()
        }

        return placesList
    }

    fun getAll(){
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                _places.value = loadPlacesWithReviews()
            } catch (e: Exception) {
            // Manejar el error
                println("PlacesViewModel: Error en getAll -> ${e.message}")

            } finally {
                _isRefreshing.value = false
            }

        }
    }

    fun getMyPlaces(ownerId:String){
        viewModelScope.launch {
            _myPlaces.value = getMyPlacesFirebase(ownerId)
        }
    }

    private suspend fun getMyPlacesFirebase(ownerId: String): List<Place>{
        val snapshot = db.collection("places")
            .whereEqualTo("ownerId", ownerId)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(Place::class.java)?.apply {
                this.id = it.id
            }
        }
    }

    private suspend fun getAllFirebase(): List<Place>{
        val snapshot = db.collection("places")
            .get()
            .await()
        println("snapshot: " + snapshot.documents)

        return snapshot.documents.mapNotNull {
            it.toObject(Place::class.java)?.apply {
                println("ids" + this.id + it.id)
                this.id = it.id
            }
        }
    }



    fun create(place: Place){
        viewModelScope.launch {
            _placeResult.value = RequestResult.Loading
            _placeResult.value =  runCatching {
                createFirebase(place)
            }
                .fold(
                    onSuccess = { RequestResult.Success("Lugar creado correctamente") },
                    onFailure = { RequestResult.Failure(it.message ?: "Error al crear lugar")}
                )
        }
    }

    private suspend fun createFirebase(place: Place){
        db.collection("places")
            .add(place)
            .await()
    }

    fun findById(id: String): Place?{
        return _places.value.find { it.id == id }
    }

    fun findByType(type: PlaceType): List<Place>{
        return _places.value.filter { it.type == type }
    }

    fun findByName(name: String): List<Place>{
        return _places.value.filter { it.title.contains(name, ignoreCase = true) }
    }
    fun resetOperationResult(){
        _placeResult.value = null
    }
    fun loadPlaces(){
        _places.value = listOf(
            /*Place(
                id = "1",
                title = "Restaurante El Paisa",
                description = "El mejor restaurante de Armenia, con auténtica comida paisa.",
                address = "Cra. 14 #12N-25, Armenia, Quindío",
                location = Location(4.5518, -75.6637),
                images = listOf(
                    "https://plazaclaro.com.co/wp-content/uploads/2022/11/EL-PAISA.png",
                    "https://static.spotapps.co/website_images/ab_websites/298841_website_v1/logo.png",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0MbKRp96EmTCTmOY7VRRJu6qYj0TRYcIo5A&s"
                ),
                phones = listOf("6067356789"),
                type = PlaceType.RESTAURANTE,
                schedules = listOf(
                    Schedule(DayOfWeek.MONDAY, LocalTime(10, 0), LocalTime(20, 0))
                ),
                ownerId = "1",
                city = City.ARMENIA
            ),
            Place(
                id = "2",
                title = "Bar La Terraza",
                description = "El mejor bar con música en vivo y cocteles en el centro de Armenia.",
                address = "Calle 21 #14-45, Armenia, Quindío",
                location = Location(4.5362, -75.6729),
                images = listOf(
                    "https://cdn-blog.reservandonos.com/blog/wp-content/uploads/2024/10/07161642/mejores-bares-colombia.webp"
                ),
                phones = listOf("6067421122"),
                type = PlaceType.BAR,
                schedules = listOf(
                    Schedule(DayOfWeek.FRIDAY, LocalTime(18, 0), LocalTime(2, 0)),
                    Schedule(DayOfWeek.SATURDAY, LocalTime(18, 0), LocalTime(2, 0))
                ),
                ownerId = "2",
                city = City.ARMENIA
            ),
            Place(
                id = "3",
                title = "Café Aroma",
                description = "Un acogedor café artesanal con los mejores granos del Eje Cafetero.",
                address = "Cra. 13 #9-28, Armenia, Quindío",
                location = Location(4.5377, -75.6751),
                images = listOf(
                    "https://cdn.colombia.com/gastronomia/2022/07/22/los-mejores-cafes-de-colombia-1055459.webp",
                    "https://images.unsplash.com/photo-1510626176961-4b37d6dc83a6",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSSo4A9asXLXhX4gzy0CgWiZ4jqUSKzUqzyuA&s"
                ),
                phones = listOf("6067419988"),
                type = PlaceType.CAFETERIA,
                schedules = listOf(
                    Schedule(DayOfWeek.MONDAY, LocalTime(8, 0), LocalTime(18, 0)),
                    Schedule(DayOfWeek.SATURDAY, LocalTime(9, 0), LocalTime(16, 0))
                ),
                ownerId = "3",
                city = City.ARMENIA
            ),
            Place(
                id = "4",
                title = "Hotel Mirador de Armenia",
                description = "Hotel con vista panorámica a las montañas y desayuno incluido.",
                address = "Cl. 2 #16-40, Armenia, Quindío",
                location = Location(4.5204, -75.6961),
                images = listOf(
                    "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/1f/4b/8a/hotel-mirador.jpg",
                    "https://images.unsplash.com/photo-1551776235-dde6d4829808",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRL1NVMEULBThlO-1HQqYQEnwrp4tPUL9D6lA&s"
                ),
                phones = listOf("6067314455"),
                type = PlaceType.OTROS,
                schedules = listOf(
                    Schedule(DayOfWeek.MONDAY, LocalTime(0, 0), LocalTime(23, 59))
                ),
                ownerId = "4",
                city = City.ARMENIA
            ),
            Place(
                id = "5",
                title = "Gimnasio Vital Fitness",
                description = "Centro de entrenamiento con pesas, cardio y clases funcionales.",
                address = "Av. Bolívar #17N-15, Armenia, Quindío",
                location = Location(4.5469, -75.6593),
                images = listOf(
                    "https://www.bodytech.com.co/imagenes/gimnasios/clubes/gym.webp",
                    "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSk6ZpZ3S-l5aZQ7V6tR3PzSFnULpy-mWzktQ&s"
                ),
                phones = listOf("6067395544"),
                type = PlaceType.OTROS,
                schedules = listOf(
                    Schedule(DayOfWeek.MONDAY, LocalTime(6, 0), LocalTime(22, 0)),
                    Schedule(DayOfWeek.SATURDAY, LocalTime(8, 0), LocalTime(20, 0))
                ),
                ownerId = "5",
                city = City.ARMENIA
            ),*/


        )
    }
}