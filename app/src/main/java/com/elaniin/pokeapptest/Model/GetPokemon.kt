package com.elaniin.pokeapptest.Model

class GetPokemon(private val services:Services) {
    suspend fun invoke(name:String) = services.getPokemonInfo(name)
}