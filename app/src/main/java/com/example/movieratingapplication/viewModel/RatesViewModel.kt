package com.example.movieratingapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

// Data class for holding current rating information
data class Rating(val averageRating: Float, val numberOfRatings: Int)

class RatesViewModel() : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData for current rating
    private val _currentRating = MutableLiveData<Rating>()
    val currentRating: LiveData<Rating> get() = _currentRating

    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    val isLoading = MutableLiveData<Boolean>()


    private var ratingsListener: ListenerRegistration? = null

    // Function to fetch ratings for the current movie
    fun fetchRates(movie_id : String?) {
        if (movie_id == null) {
            _errorMessage.value = "Movie ID is missing."
            return
        }
        val ratingsRef = db.collection("movies").document(movie_id).collection("ratings")

        // Add a snapshot listener to get real-time updates
        ratingsListener = ratingsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                _errorMessage.value = "Error fetching ratings: ${error.localizedMessage}"
            } else if (snapshot != null) {
                val ratingsList = snapshot.documents.mapNotNull { doc ->
                    doc.getDouble("rate")?.toFloat()
                }

                val averageRating = if (ratingsList.isNotEmpty()) {
                    ratingsList.sum() / ratingsList.size
                } else {
                    0.0f
                }
                val numberOfRatings = ratingsList.size
                _currentRating.value = Rating(averageRating, numberOfRatings)
            }
        }
    }

    // Function to submit user's rating
    fun submitUserRating(rating: Float, movie_id : String?) {
        if (movie_id == null) {
            _errorMessage.value = "Movie ID is missing."
            return
        }
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _errorMessage.value = "User not logged in."
            return
        }

        val ratingData = hashMapOf(
            "rate" to rating
        )

        val ratingRef = db.collection("movies").document(movie_id)
            .collection("ratings").document(userId)

        ratingRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                _errorMessage.value = "You have already rated this movie."
            } else {
                val ratingData = hashMapOf(
                    "rate" to rating
                )

                ratingRef.set(ratingData)
                    .addOnSuccessListener {
                        _errorMessage.value = null // Clear any previous error
                    }
                    .addOnFailureListener { e ->
                        _errorMessage.value = "Error submitting rating: ${e.localizedMessage}"
                    }
            }
        }.addOnFailureListener { e ->
            _errorMessage.value = "Error checking existing rating: ${e.localizedMessage}"
        }
    }

    // Function to delete user's rating
    fun deleteUserRating(movie_id : String?) {
        if (movie_id == null) {
            _errorMessage.value = "Movie ID is missing."
            return
        }
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _errorMessage.value = "User not logged in."
            return
        }

        val ratingRef = db.collection("movies").document(movie_id)
            .collection("ratings").document(userId)

        ratingRef.delete()
            .addOnSuccessListener {
                _errorMessage.value = null // Clear any previous error
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Error deleting rating: ${e.localizedMessage}"
            }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove the listener to prevent memory leaks
        ratingsListener?.remove()
    }
}
