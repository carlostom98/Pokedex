package com.example.dailyjobs.Model

import com.example.dailyjobs.Model.PokemonDataModel.PokemonModel
import retrofit2.Retrofit

class Services(private val retrofitService: ApiService) {
    suspend fun getPokemonInfo(id: Int): PokemonModel? {
        return retrofitService.getPokemon(id).body()
    }
}