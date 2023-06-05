package com.elaniin.pokeapptest.FirebaseDataBase

import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry
import kotlinx.coroutines.flow.Flow

interface DataBaseImplementation {
    fun addPokemon(pokemonListEntry: PokemonListEntry)
    fun removePokemon(pokemonListEntry: PokemonListEntry)
    fun saveData(nameGroup: String)
    fun removeData()
     fun retrieveData(callback:(List<DataBasePokemon>)->Unit)
}