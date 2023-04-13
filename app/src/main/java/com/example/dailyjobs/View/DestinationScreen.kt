package com.example.dailyjobs.View

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class DestinationScreen(
    private val route: String,
    private val navArgs: List<NavArgs> = emptyList(),
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
        DestinationScreen("PokemonDetailScreen", listOf(NavArgs.DominantColor, NavArgs.PokemonName))
}

private enum class NavArgs(val key: String, val type: NavType<*>) {
    DominantColor("dominantColor", NavType.IntType),
    PokemonName("pokemonName", NavType.StringType)
}