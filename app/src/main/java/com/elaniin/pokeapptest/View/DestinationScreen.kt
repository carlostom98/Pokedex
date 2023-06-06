package com.elaniin.pokeapptest.View

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

    object PokedexScreen : DestinationScreen("PokedexScreen", listOf(NavArgs.UserId)){
        fun withUserId(userId:String)= userId.let {  "$route/$it"  }
    }
    object RegisterScreen : DestinationScreen("RegisterScreen")
    object PokemonDetailScreen :
        DestinationScreen(
            "PokemonDetailScreen",
            listOf(NavArgs.UserId)
        ) {
        fun withUserId(userId:String)= userId.let {  "$route/$it"  }
    }
}

enum class NavArgs(val key: String, val type: NavType<*>) {
    UserId("userID", NavType.StringType),
    PokemonName("pokemonName", NavType.StringType),
    PokemonImage("urlImage", NavType.StringType)
}
