package com.example.jetmovie.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetmovie.ui.detail.MovieDetailScreen
import com.example.jetmovie.ui.favorites.FavoritesScreen
import com.example.jetmovie.ui.home.HomeScreen
import com.example.jetmovie.ui.login.LoginScreen
import com.example.jetmovie.ui.login.RegisterScreen
import com.example.jetmovie.ui.account.AccountScreen
import com.example.jetmovie.utils.K
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MovieNavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    auth: FirebaseAuth
) {

    val startDestination = if (auth.currentUser != null) {
        Route.HomeScreen().route
    } else {
        Route.LoginScreen().route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize()
    ) {
        // --- LOGIN ---
        composable(
            route = Route.LoginScreen().route,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + shrinkOut() }
        ) {
            LoginScreen(
                auth = auth,
                onLoginSuccess = {
                    navController.navigate(Route.HomeScreen().route) {
                        popUpTo(Route.LoginScreen().route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = {
                    navController.navigate(Route.RegisterScreen().route)
                }
            )
        }

        // --- REGISTER ---
        composable(route = Route.RegisterScreen().route) {
            RegisterScreen(
                auth = auth,
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // --- HOME ---
        composable(route = Route.HomeScreen().route) {
            HomeScreen(
                onMovieClick = { id ->
                    navController.navigate(Route.FilmScreen().getRouteWithArgs(id = id)) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                    }
                },
                onFavoritesClick = {
                    navController.navigate(Route.FavoritesScreen().route)
                },
                onAccountClick = {
                    navController.navigate(Route.AccountScreen().route)
                }
            )
        }

        // --- MOVIE DETAIL ---
        composable(
            route = Route.FilmScreen().routeWithArgs,
            arguments = listOf(navArgument(name = K.MOVIE_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(K.MOVIE_ID) ?: return@composable
            MovieDetailScreen(
                movieId = movieId,
                onNavigateUp = { navController.navigateUp() },
                onMovieClick = { id ->
                    navController.navigate(Route.FilmScreen().getRouteWithArgs(id)) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                    }
                },
                onActorClick = {}
            )
        }

        // --- FAVORITES ---
        composable(route = Route.FavoritesScreen().route) {
            FavoritesScreen(
                onMovieClick = { id ->
                    navController.navigate(Route.FilmScreen().getRouteWithArgs(id = id))
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        // --- ACCOUNT ---
        composable(route = Route.AccountScreen().route) {
            AccountScreen(
                onBack = { navController.popBackStack() },
                onSignOut = {
                    navController.navigate(Route.LoginScreen().route) {
                        popUpTo(Route.HomeScreen().route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
