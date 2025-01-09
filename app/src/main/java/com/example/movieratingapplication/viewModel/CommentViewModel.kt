package com.example.movieratingapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

// Data class for holding comment information
data class Comment(
    val userId: String,
    val commentText: String,
    val timestamp: Long, // For ordering comments by time
    val commentId: String // Firestore document ID
)

class CommentViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private var commentsListener: ListenerRegistration? = null

    // Fetch all comments for the current movie
    fun fetchComments(movieId: String?) {
        if (movieId.isNullOrEmpty()) {
            _errorMessage.value = "Movie ID is missing."
            return
        }

        val commentsRef = db.collection("movies").document(movieId).collection("comments")

        commentsListener = commentsRef.orderBy("timestamp").addSnapshotListener { snapshot, error ->
            if (error != null) {
                _errorMessage.value = "Error fetching comments: ${error.localizedMessage}"
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val commentsList = snapshot.documents.map { doc ->
                    Comment(
                        userId = doc.getString("userId") ?: "",
                        commentText = doc.getString("commentText") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L,
                        commentId = doc.id
                    )
                }
                _comments.value = commentsList
            }
        }
    }

    // Submit a new comment
    fun submitComment(movieId: String?, commentText: String) {
        if (movieId.isNullOrEmpty()) {
            _errorMessage.value = "Movie ID is missing."
            return
        }

        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            _errorMessage.value = "User not logged in."
            return
        }

        val commentRef = db.collection("movies").document(movieId).collection("comments").document()

        val commentData = hashMapOf(
            "userId" to userId,
            "commentText" to commentText,
            "timestamp" to System.currentTimeMillis()
        )

        commentRef.set(commentData)
            .addOnSuccessListener {
                _errorMessage.value = null // Clear any previous error
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Error submitting comment: ${e.localizedMessage}"
            }
    }

    // Delete a comment
    fun deleteComment(movieId: String?, commentId: String) {
        if (movieId.isNullOrEmpty()) {
            _errorMessage.value = "Movie ID is missing."
            return
        }

        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            _errorMessage.value = "User not logged in."
            return
        }

        val commentRef = db.collection("movies").document(movieId).collection("comments").document(commentId)

        commentRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val commentUserId = document.getString("userId")
                if (commentUserId == userId) {
                    commentRef.delete()
                        .addOnSuccessListener {
                            _errorMessage.value = null // Clear any previous error
                        }
                        .addOnFailureListener { e ->
                            _errorMessage.value = "Error deleting comment: ${e.localizedMessage}"
                        }
                } else {
                    _errorMessage.value = "You can only delete your own comments."
                }
            } else {
                _errorMessage.value = "Comment not found."
            }
        }.addOnFailureListener { e ->
            _errorMessage.value = "Error fetching comment: ${e.localizedMessage}"
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove the listener to prevent memory leaks
        commentsListener?.remove()
    }
}