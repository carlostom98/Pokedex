package com.example.dailyjobs.Model

import com.example.dailyjobs.Model.PokemonDataModel.PokemonModel

class Services() {
    private val retrofitPokemon= RetrofitServices.pokemonRetrofit()
    suspend fun getPokemonInfo(id:Int):PokemonModel?
    { return retrofitPokemon.create(ApiService::class.java).getPokemon(id).body()}
}