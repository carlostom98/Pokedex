package com.example.dailyjobs.View

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailyjobs.View.Screens.ScreenPokedex

@Composable
fun NavigateScreens(navigationHost: NavHostController) {
    NavHost(navigationHost, DestinationScreen.PokedexScreen.baseRoute) {
        composable(DestinationScreen.PokedexScreen.baseRoute) {
            ScreenPokedex(navHost = navigationHost)
        }
        composable(
            DestinationScreen.PokemonDetailScreen.baseRoute,
            arguments = DestinationScreen.PokemonDetailScreen.navArgument) {
            val dominantColor = remember{
                val color = it.arguments?.getInt("dominantColor")
                requireNotNull(color) { "Color must contain a value" }
            }
            val pokemonName = remember{
                val pokemonName =it.arguments?.getString("pokemonName")
                requireNotNull(pokemonName){"PokemonName is required"}
            }
        }
    }
}