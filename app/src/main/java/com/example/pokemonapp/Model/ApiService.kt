package com.example.pokemonapp.Model

import com.example.pokemonapp.Model.Pokemon2Model.PokedexProperties
import com.example.pokemonapp.Model.Pokemon2Model.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Get Pokemon
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name")name:String): PokedexProperties
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset:Int): PokemonList
}