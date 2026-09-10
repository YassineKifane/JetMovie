package com.example.jetmovie.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jetmovie.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetmovie.ui.components.LoadingView
import com.example.jetmovie.ui.home.components.BodyContent
import com.example.jetmovie.ui.home.components.TopContent
import com.example.jetmovie.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlin.math.ceil

val defaultPadding = 16.dp
val itemSpacing = 8.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (id: Int) -> Unit,
    onFavoritesClick: () -> Unit,
    onAccountClick: () -> Unit
) {
    val context = LocalContext.current
    val hasInternet = remember { mutableStateOf(NetworkUtils.isInternetAvailable(context)) }

    LaunchedEffect(Unit) {
        if (!hasInternet.value) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
        }
    }

    val state by homeViewModel.homeState.collectAsStateWithLifecycle()
    val searchState by homeViewModel.searchState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    val isSearching = showSearch && searchQuery.isNotBlank()
    val displayedMovies = if (isSearching) searchState.discoverMovies else state.discoverMovies
    val isLoading = if (isSearching) searchState.isLoading else state.isLoading
    val error = if (isSearching) searchState.error else state.error
    var mapJustOpened by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf("home") }
    Scaffold(
        containerColor = Color(0xFF000000),
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    shadowElevation = 16.dp,
                    color = Color(0xFF121212).copy(alpha = 0.92f),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(0.86f)
                            .height(58.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showSearch = !showSearch },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = if (showSearch) Color(0xFFE50914) else Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                activeTab = "map"
                                mapJustOpened = true
                                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=cinema near me"))
                                context.startActivity(mapIntent)

                            },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "Map",
                                tint = if (activeTab == "map") Color(0xFFE50914) else Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        LaunchedEffect(mapJustOpened) {
                            if (mapJustOpened) {
                                delay(750)
                                activeTab = "home"
                                mapJustOpened = false
                            }
                        }
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                                onFavoritesClick()
                            },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Favorites",
                                tint = if (isFavorite) Color(0xFFE50914) else Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                activeTab = "account"
                                onAccountClick()
                                      },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Account",
                                tint = if (activeTab == "account") Color(0xFFE50914) else Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.jetmovie),
                contentDescription = "Jetmovie Logo",
                modifier = Modifier.height(70.dp)
            )

            if (showSearch) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        homeViewModel.searchMoviesDebounced(it)
                    },
                    label = { Text("Search movie") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = defaultPadding, vertical = 8.dp)
                )
            }

            when {
                isLoading -> {
                    LoadingView(isLoading = true)
                }
                error != null -> {
                    Text(
                        text = error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                displayedMovies.isEmpty() -> {
                    Text(
                        text = "No results found.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    var isAutoScrolling by remember { mutableStateOf(true) }
                    val pagerState = rememberPagerState(
                        initialPage = 0,
                        pageCount = { displayedMovies.size }
                    )
                    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
                    LaunchedEffect(key1 = pagerState.currentPage) {
                        if (isDragged) {
                            isAutoScrolling = false
                        } else {
                            isAutoScrolling = true
                            delay(5000)
                            with(pagerState) {
                                val target = if (currentPage < displayedMovies.size - 1) currentPage + 1 else 0
                                scrollToPage(target)
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxSize()) {
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            val boxHeight = maxHeight
                            val topItemHeight = boxHeight * .55f
                            val bodyItemHeight = boxHeight * .45f
                            HorizontalPager(
                                state = pagerState,
                                contentPadding = PaddingValues(defaultPadding),
                                pageSize = PageSize.Fill,
                                pageSpacing = itemSpacing
                            ) { page ->
                                if (isAutoScrolling) {
                                    AnimatedContent(
                                        targetState = page,
                                        label = "",
                                    ) { index ->
                                        TopContent(
                                            modifier = Modifier
                                                .align(Alignment.TopCenter)
                                                .heightIn(min = topItemHeight),
                                            movie = displayedMovies[index],
                                            onMovieClick = { onMovieClick(it) }
                                        )
                                    }
                                } else {
                                    TopContent(
                                        modifier = Modifier
                                            .align(Alignment.TopCenter)
                                            .heightIn(min = topItemHeight),
                                        movie = displayedMovies[page],
                                        onMovieClick = { onMovieClick(it) }
                                    )
                                }
                            }
                            BodyContent(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .heightIn(max = bodyItemHeight),
                                discoverMovies = displayedMovies,
                                trendingMovies = state.trendingMovies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }
                }
            }
        }
    }
}
