package com.example.dailyjobs.Model

import com.example.dailyjobs.Model.PokemonDataModel.PokemonModel
import org.koin.androidx.compose.inject

class Services(private val retrofitPokemon:ApiService) {

    suspend fun getPokemonInfo(id:Int):PokemonModel?
    { return retrofitPokemon.getPokemon(id).body()}
}