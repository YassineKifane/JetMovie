package com.example.jetmovie.ui.detail
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.jetmovie.ui.favorites.FavoriteRepository
import com.example.jetmovie.ui.favorites.FavoriteMovie
import com.example.jetmovie.ui.detail.components.DetailBodyContent
import com.example.jetmovie.ui.detail.components.DetailTopContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetmovie.ui.detail.DetailViewModel
import com.example.jetmovie.utils.K

@Composable
fun MovieDetailScreen(
    movieId: Int,
    modifier: Modifier = Modifier,
    movieDetailViewModel: DetailViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onActorClick: (Int) -> Unit
) {
    LaunchedEffect(movieId) {
        movieDetailViewModel.fetchMovieDetail(movieId)
    }

    val state by movieDetailViewModel.detailState.collectAsStateWithLifecycle()
    val movie = state.movieDetail

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var isFavorite by remember { mutableStateOf(false) }
    val repo = remember { FavoriteRepository() }

// Réinitialise à chaque navigation
    LaunchedEffect(userId, movieId) {
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("User")
                .document(userId)
                .collection("bookmarks")
                .document(movieId.toString())
                .get()
                .addOnSuccessListener { doc -> isFavorite = doc.exists() }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(state.error != null, modifier = Modifier.align(Alignment.TopCenter)) {
            Text(
                state.error ?: "unknown",
                color = MaterialTheme.colorScheme.error,
                maxLines = 2
            )
        }

        AnimatedVisibility(visible = !state.isLoading && state.error == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                movie?.let { movieDetail ->
                    Column(modifier = Modifier.fillMaxSize()) {
                        DetailTopContent(
                            movieDetail = movieDetail,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.4f)
                        )
                        DetailBodyContent(
                            movieDetail = movieDetail,
                            movies = state.movies,
                            isMovieLoading = state.isMovieLoading,
                            fetchMovies = movieDetailViewModel::fetchMovie,
                            onMovieClick = onMovieClick,
                            onActorClick = onActorClick,
                            isFavorite = isFavorite,
                            onFavoriteClick = { wantFavorite ->
                                isFavorite = wantFavorite
                                if (wantFavorite) {
                                    repo.addFavorite(FavoriteMovie(movieId, movieDetail.title, K.BASE_IMAGE_URL + movieDetail.posterPath))
                                } else {
                                    repo.removeFavorite(movieId)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.6f)
                        )
                    }
                }
            }
        }

        IconButton(onClick = onNavigateUp, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }

    // Loading indicator
    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
