package com.example.dailyjobs.View

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailyjobs.View.Screens.ScreenPokedex

@Composable
fun NavigateScreens(navigationHost:NavHostController){
    NavHost(navigationHost, DestinationScreen.PokedexScreen.route){
        composable(DestinationScreen.PokedexScreen.route){
            ScreenPokedex(navHost = navigationHost)
        }
    }
}