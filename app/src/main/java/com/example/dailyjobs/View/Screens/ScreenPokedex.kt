package com.example.dailyjobs.View.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dailyjobs.ViewModel.PokemonViewModel.PokemonViewModel
import com.example.dailyjobs.ViewModel.PokemonViewModel.ViewModelsFactory

@Composable
fun ScreenPokedex(navHost:NavHostController){
    val pokemonViewModel= viewModel<PokemonViewModel>(factory = ViewModelsFactory)
    val pokemon by pokemonViewModel.pokemonInfo.observeAsState()
    var pokemonName by remember { mutableStateOf("") }
    Box(contentAlignment = Alignment.Center){
        Button(onClick = {pokemonViewModel.getPokemonInfo(1)}) {
            Text(text = "Update Pokemon")
        }
        Text(text =pokemonName )
    }

    LaunchedEffect(pokemon){
       pokemon?.let {
           pokemonName=it.name
       }
    }
}