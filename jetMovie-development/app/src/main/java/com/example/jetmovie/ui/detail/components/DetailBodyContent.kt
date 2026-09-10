package com.example.jetmovie.ui.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetmovie.movie.domain.models.Movie
import com.example.jetmovie.movie_detail.domain.models.MovieDetail
import com.example.jetmovie.movie_detail.domain.models.Review
import com.example.jetmovie.ui.favorites.FavoriteMovie
import com.example.jetmovie.ui.favorites.FavoriteRepository
import com.example.jetmovie.ui.home.components.MovieCard
import com.example.jetmovie.ui.home.components.MovieCoverImage
import com.example.jetmovie.ui.home.defaultPadding
import com.example.jetmovie.ui.home.itemSpacing
import com.example.jetmovie.utils.K
import com.google.accompanist.flowlayout.FlowRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



@Composable
fun MovieInfoFlowInlineBlock(
    title: String,
    values: List<String>
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        mainAxisSpacing = 4.dp,
        crossAxisSpacing = 4.dp
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        values.forEachIndexed { i, v ->
            Text(
                text = v,
                style = MaterialTheme.typography.bodySmall
            )
            if (i != values.lastIndex) {
                Text(
                    text = " - ",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun DetailBodyContent(
    modifier: Modifier = Modifier,
    movieDetail: MovieDetail,
    movies: List<Movie>,
    isMovieLoading: Boolean,
    fetchMovies: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onActorClick: (Int) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: (Boolean) -> Unit
) {
    // Firestore Repository custom
    val repo = remember { FavoriteRepository() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid


    val trailerKey = movieDetail.videos?.results
        ?.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }
        ?.key

    val openDialog = remember { mutableStateOf(false) }
    // Limite à 4 acteurs et centre
    val mainCast = movieDetail.cast.take(4)






    LazyColumn(modifier) {

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(defaultPadding)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()

                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            movieDetail.genreIds.forEachIndexed { index, genreText ->
                                Text(
                                    text = genreText,
                                    modifier = Modifier,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp,
                                    )
                                )
                                // Show divider after all except the last item
                                if (index != movieDetail.genreIds.lastIndex) {
                                    Text(
                                        text = " \u2022 ",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                        Text(
                            text = movieDetail.runTime,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(itemSpacing))
                    Text(
                        text = movieDetail.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(itemSpacing))
                    Text(
                        text = movieDetail.overview,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.height(itemSpacing))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = { onFavoriteClick(!isFavorite) },
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                contentDescription = "Favorite"
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(itemSpacing))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = itemSpacing),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cast & Crew",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        IconButton(onClick = { openDialog.value = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Cast & Crew"
                            )
                        }
                    }

                    val mainCast = movieDetail.cast.take(4)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        mainCast.forEach { actor ->
                            ActorItem(
                                cast = actor,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .clickable { onActorClick(actor.id) }
                            )
                        }
                    }



                    if (openDialog.value) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { openDialog.value = false },
                            title = {
                                Text(text = "All Cast & Crew")
                            },
                            text = {
                                LazyColumn(
                                    modifier = Modifier
                                        .heightIn(max = 400.dp)
                                        .fillMaxWidth()
                                ) {
                                    movieDetail.cast.chunked(2).forEach { actorPair ->
                                        item {
                                            Column {
                                                Row(
                                                    Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    actorPair.forEach { actor ->
                                                        ActorItem(
                                                            cast = actor,
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .clickable { /* action ici si besoin */ }
                                                        )
                                                    }
                                                    if (actorPair.size == 1) {
                                                        Spacer(modifier = Modifier.weight(1f))
                                                    }
                                                }
                                                // Espacement entre chaque ligne
                                                Spacer(modifier = Modifier.height(30.dp)) // ajuste la valeur à ton goût
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { openDialog.value = false }) {
                                    Text("Close")
                                }
                            }
                        )
                    }



                    // après LazyRow du cast
                    Spacer(modifier = Modifier.height(itemSpacing)) // l'espace par défaut
                    Spacer(modifier = Modifier.height(itemSpacing))
                    // Ligne de séparation stylisée
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = itemSpacing), // optionnel : espacement sur les côtés
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.outline // ou une couleur personnalisée
                    )

                    Spacer(modifier = Modifier.height(itemSpacing)) // espace après la ligne
                    Spacer(modifier = Modifier.height(itemSpacing))

                    MovieInfoFlowInlineBlock(
                        title = "Spoken language: ",
                        values = movieDetail.language
                    )
                    Spacer(modifier = Modifier.height(itemSpacing))
                    MovieInfoFlowInlineBlock(
                        title = "Production countries: ",
                        values = movieDetail.productionCountry
                    )
                    Spacer(modifier = Modifier.height(itemSpacing))

                    // après LazyRow du cast
                    Spacer(modifier = Modifier.height(itemSpacing)) // l'espace par défaut
                    Spacer(modifier = Modifier.height(itemSpacing))
                    // Ligne de séparation stylisée
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = itemSpacing), // optionnel : espacement sur les côtés
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.outline // ou une couleur personnalisée
                    )

                    Spacer(modifier = Modifier.height(itemSpacing)) // espace après la ligne
                    Spacer(modifier = Modifier.height(itemSpacing))


                    Text(
                        text = "Reviews",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(itemSpacing))
                    Review(reviews = movieDetail.reviews)
                    Spacer(modifier = Modifier.height(itemSpacing))
                    MoreLikeThis(
                        fetchMovies = fetchMovies,
                        isMovieLoading = isMovieLoading,
                        movies = movies,
                        onMovieClick = onMovieClick
                    )

                }
            }
        }


    }

}


@Composable
fun MoreLikeThis(
    modifier: Modifier = Modifier,
    fetchMovies: () -> Unit,
    isMovieLoading: Boolean,
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        fetchMovies()
    }
    Column(modifier) {
        Text(
            text = "More like this",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        LazyRow {
            item {
                AnimatedVisibility(visible = isMovieLoading) {
                    CircularProgressIndicator()
                }
            }
            items(movies) {
                MovieCoverImage(movie = it, onMovieClick = onMovieClick)
            }
        }
    }

}

private enum class ActionIcon(val icon: ImageVector, val contentDescription: String) {
    BookMark(icon = Icons.Default.Bookmark, "bookmark"),
   }

@Composable
private fun ActionIconBtn(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    bgColor: Color = Color.Black.copy(.8f)
) {
    MovieCard(
        shapes = CircleShape,
        modifier = modifier
            .padding(4.dp),
        bgColor = bgColor
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
private fun MovieInfoItem(infoItem: List<String>, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(4.dp))
        infoItem.forEachIndexed { index, language ->
            Text(
                text = language,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
            // N’ajoute un tiret que si ce n’est pas le dernier élément
            if (index != infoItem.lastIndex) {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun Review(
    modifier: Modifier = Modifier,
    reviews: List<Review>
) {
    if (reviews.isEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), // un padding un peu plus grand, si tu veux
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No Reviews Yet.",
                style = MaterialTheme.typography.titleMedium, // plus grand que bodyMedium
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant // optionnel : couleur plus douce
            )
        }
        return
    }

    val (viewMore, setViewMore) = remember {
        mutableStateOf(false)
    }
    // show only three reviews or less by default
    val defaultReview =
        if (reviews.size > 3) reviews.take(3) else reviews
    // show more when user needs more review
    val movieReviews = if (viewMore) reviews else defaultReview
    val btnText = if (viewMore) "Collapse" else "More..."
    Column(modifier) {
        movieReviews.forEach { review ->
            ReviewItem(review = review)
            Spacer(modifier = Modifier.height(itemSpacing))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(itemSpacing))
        }
        if (reviews.size > 3) {
            TextButton(onClick = { setViewMore(!viewMore) }) {
                Text(text = btnText)
            }
        }
    }

}