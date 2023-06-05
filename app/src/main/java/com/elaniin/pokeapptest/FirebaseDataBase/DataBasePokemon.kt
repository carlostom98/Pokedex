package com.elaniin.pokeapptest.FirebaseDataBase

import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry

data class DataBasePokemon(val groupName:String, var pokemonValues:MutableList<PokemonListEntry>?=null)
