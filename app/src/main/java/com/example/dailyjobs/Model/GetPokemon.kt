package com.example.dailyjobs.Model

class GetPokemon(private val services:Services) {
    suspend fun invoke(id:Int) = services.getPokemonInfo(id)
}