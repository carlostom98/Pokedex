package com.example.dailyjobs.Model

import com.example.dailyjobs.Model.Pokemon2Model.PokedexProperties
import com.example.dailyjobs.Model.Pokemon2Model.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Get Pokemon
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id")id:Int): PokedexProperties

    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset:Int): PokemonList
}