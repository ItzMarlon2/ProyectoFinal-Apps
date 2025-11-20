package com.example.proyectofinal.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.model.Review
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReviewsViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    fun getReviewsByPlaceId(placeId: String) {
        viewModelScope.launch {
            try {
                _reviews.value = getReviewsByPlaceIdFirebase(placeId)
            } catch (e: Exception) {
                _reviews.value = emptyList()
            }
        }
    }

    private suspend fun getReviewsByPlaceIdFirebase(placeId: String): List<Review> {
        val snapshot = db.collection("reviews")
            .whereEqualTo("placeId", placeId)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(Review::class.java)?.apply {
                this.id = document.id
            }
        }
    }

    fun createReview(review: Review, onComplete: (Review) -> Unit) {
        viewModelScope.launch {
            try {
                val documentReference = db.collection("reviews").add(review).await()

                val newReviewSnapshot = documentReference.get().await()
                val createdReview = newReviewSnapshot.toObject(Review::class.java)?.apply {
                    id = newReviewSnapshot.id
                }

                if (createdReview != null) {
                    onComplete(createdReview)
                }

            } catch (e: Exception) {
            }
        }
    }
}
