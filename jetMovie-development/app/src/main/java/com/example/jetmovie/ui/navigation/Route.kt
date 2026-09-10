package com.example.jetmovie.ui.navigation

import com.example.jetmovie.utils.K

sealed class Route {
    data class HomeScreen(val route: String = "homeScreen") : Route()
    data class FilmScreen(
        val route: String = "filmScreen",
        val routeWithArgs: String = "$route/{${K.MOVIE_ID}}"
    ) : Route() {
        fun getRouteWithArgs(id: Int): String {
            return "$route/$id"
        }
    }
    data class LoginScreen(val route: String = "loginScreen") : Route()
    data class RegisterScreen(val route: String = "registerScreen") : Route()
    data class FavoritesScreen(val route: String = "favoritesScreen") : Route()
    data class AccountScreen(val route: String = "accountScreen") : Route()
}
