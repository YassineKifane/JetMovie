package com.example.jetmovie.ui.favorites

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FavoriteRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun addFavorite(movie: FavoriteMovie, onResult: (Boolean) -> Unit = {}) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("User")
            .document(userId)
            .collection("bookmarks")
            .document(movie.id.toString())
            .set(movie)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun removeFavorite(movieId: Int, onResult: (Boolean) -> Unit = {}) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("User")
            .document(userId)
            .collection("bookmarks")
            .document(movieId.toString())
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getFavorites(onComplete: (List<FavoriteMovie>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        firestore.collection("User")
            .document(userId)
            .collection("bookmarks")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { it.toObject(FavoriteMovie::class.java) }
                onComplete(list)
            }
            .addOnFailureListener { onComplete(emptyList()) }
    }
}
