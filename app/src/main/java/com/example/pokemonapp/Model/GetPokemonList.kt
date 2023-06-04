package com.example.pokemonapp.Model

class GetPokemonList(private val services: Services) {
    suspend fun invoke(limit:Int, offset:Int) = services.getPokemonList(limit, offset)
}