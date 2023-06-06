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
        composable(DestinationScreen.PokedexScreen.baseRoute, arguments = DestinationScreen.PokedexScreen.navArgument) {backEntry->
            val userId= remember {
                backEntry.arguments?.getString(NavArgs.UserId.key)
            }
            ScreenPokedex(navHost = navigationHost, userId=userId!!)
        }
        composable(
            DestinationScreen.PokemonDetailScreen.baseRoute,
            arguments = DestinationScreen.PokemonDetailScreen.navArgument
        ) { backStackEntry ->
            val userId = remember {
                backStackEntry.arguments?.getString(NavArgs.UserId.key)
            }
            PokemonInformation(userId = userId!!)
        }
    }
}