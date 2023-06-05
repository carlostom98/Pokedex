package com.elaniin.pokeapptest.View.Screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import com.elaniin.pokeapptest.ViewModel.PokemonViewModel.DataBaseManagerViewModel
import org.koin.androidx.compose.get
import kotlin.math.log

@Composable
fun PokemonInformation(pokemonName:String?){
    val databaseManager: DataBaseManagerViewModel = get()
    databaseManager.getAllPokemonsInDB()
   val pokemonsList by databaseManager.pokemonFromDB.observeAsState()
    Box(contentAlignment = Alignment.Center){
        Text(text = pokemonName!!)
    }
    LaunchedEffect(key1 = pokemonsList ){
        if (pokemonsList!=null){
            Log.d("Pokemons", "$pokemonsList")
        }
    }
}