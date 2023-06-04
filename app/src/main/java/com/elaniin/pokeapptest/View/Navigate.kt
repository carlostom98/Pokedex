package com.elaniin.pokeapptest.View

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elaniin.pokeapptest.MainActivity
import com.elaniin.pokeapptest.View.Screens.PokemonInformation
import com.elaniin.pokeapptest.View.Screens.RegisterScreen
import com.elaniin.pokeapptest.View.Screens.ScreenPokedex

@Composable
fun NavigateScreens(navigationHost: NavHostController, activity:MainActivity) {
    NavHost(navigationHost, DestinationScreen.RegisterScreen.baseRoute) {
        composable(DestinationScreen.RegisterScreen.baseRoute) {
            RegisterScreen(navHost = navigationHost)
        }
        composable(DestinationScreen.PokedexScreen.baseRoute) {
            ScreenPokedex(navHost = navigationHost)
        }
        composable(
            DestinationScreen.PokemonDetailScreen.baseRoute,
            arguments = DestinationScreen.PokemonDetailScreen.navArgument
        ) { backStackEntry ->
            val pokemonName = remember {
                backStackEntry.arguments?.getString(NavArgs.PokemonName.key)
            }
            PokemonInformation(pokemonName = pokemonName)
        }
    }
}