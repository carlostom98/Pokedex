package com.example.dailyjobs.Model

class GetPokemon(var id:Int) {
    private val servicePokemon = Services()
    suspend fun invoke() = servicePokemon.getPokemonInfo(id)
}