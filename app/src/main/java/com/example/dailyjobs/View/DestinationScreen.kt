package com.example.dailyjobs.View

import android.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class DestinationScreen(
    val route: String,
    val navArgs: List<NavArgs> = emptyList(),
) {
    val baseRoute = run {
        val argsKeys = navArgs.map { "{${it.key}}" }
        listOf(route).plus(argsKeys).joinToString("/")
    }
    val navArgument = navArgs.map {
        navArgument(it.key) { it.type }
    }

    object PokedexScreen : DestinationScreen("PokedexScreen")
    object PokemonDetailScreen :
        DestinationScreen(
            "PokemonDetailScreen",
            listOf(NavArgs.PokemonName)
        ) {
        fun withPokemonName(
            pokemonName: String?,
            dominantColor: androidx.compose.ui.graphics.Color?,
        ) = pokemonName?.let { "$route/$it" }
            ?: "$route/Not founded Pokemon"
    }
}

enum class NavArgs(val key: String, val type: NavType<*>) {
    DominantColor("dominantColor", NavType.IntType),
    PokemonName("pokemonName", NavType.StringType),
    PokemonImage("urlImage", NavType.StringType)
}
