package com.example.dailyjobs.Model

import com.example.dailyjobs.Model.PokemonDataModel.PokemonModel
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // Get Pokemon
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id")id:Int): retrofit2.Response <PokemonModel>
}