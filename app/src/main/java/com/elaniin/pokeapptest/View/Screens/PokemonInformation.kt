package com.elaniin.pokeapptest.View.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun PokemonInformation(pokemonName:String?){
    Box(contentAlignment = Alignment.Center){
        Text(text = pokemonName!!)
    }
}