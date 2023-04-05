package com.example.dailyjobs.View

sealed class DestinationScreen(val route:String){
    object PokedexScreen:DestinationScreen("PokedexScreen")
}
