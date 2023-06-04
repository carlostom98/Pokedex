package com.example.pokemonapp.Model.Pokemon2Model

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)