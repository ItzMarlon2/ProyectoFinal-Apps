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
import com.google.firebase.firestore.FieldValue
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

    private suspend fun loadReviewsForPlaces(places: List<Place>): List<Place> {
        if (places.isEmpty()) {
            return places
        }

        val placeIds = places.map { it.id }

        val reviewsSnapshot = db.collection("reviews")
            .whereIn("placeId", placeIds)
            .get()
            .await()
        val relevantReviews = reviewsSnapshot.mapNotNull { it.toObject(Review::class.java) }

        val reviewsByPlaceId = relevantReviews.groupBy { it.placeId }

        places.forEach { place ->
            place.reviews = reviewsByPlaceId[place.id] ?: emptyList()
        }

        return places
    }

    fun getAll(){
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                var placesList = getAllFirebase()
                _places.value = loadReviewsForPlaces(placesList)
            } catch (e: Exception) {

            } finally {
                _isRefreshing.value = false
            }

        }
    }

    fun getMyPlaces(ownerId:String){
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val myPlacesList = getMyPlacesFirebase(ownerId)
                _myPlaces.value = loadReviewsForPlaces(myPlacesList)
            } catch (e: Exception) {
            } finally {
                _isRefreshing.value = false
            }
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

    fun toggleFavorite(placeId: String, userId: String) {
        viewModelScope.launch {
            try {
                val placeRef = db.collection("places").document(placeId)

                db.runTransaction { transaction ->
                    val snapshot = transaction.get(placeRef)
                    val favoritedByList = snapshot.get("favoritedBy") as? List<String> ?: emptyList()

                    if (favoritedByList.contains(userId)) {
                        transaction.update(placeRef, "favoritedBy",
                            FieldValue.arrayRemove(userId))
                    } else {
                        transaction.update(placeRef, "favoritedBy",
                            FieldValue.arrayUnion(userId))
                    }
                    null
                }.await()

                updateLocalPlaceFavoriteState(placeId, userId)

            } catch (e: Exception) {
                println("Error en toggleFavorite: ${e.message}")
            }
        }
    }

    private fun updateLocalPlaceFavoriteState(placeId: String, userId: String) {
        _places.value = _places.value.map { place ->
            if (place.id == placeId) {
                val newFavList = if (place.favoritedBy.contains(userId)) {
                    place.favoritedBy - userId
                } else {
                    place.favoritedBy + userId
                }
                place.copy(favoritedBy = newFavList)
            } else {
                place
            }
        }

    }

    private suspend fun createFirebase(place: Place){
        db.collection("places")
            .add(place)
            .await()
    }

    fun findByName(name: String): List<Place>{
        return _places.value.filter { it.title.contains(name, ignoreCase = true) }
    }
    fun resetOperationResult(){
        _placeResult.value = null
    }
}