package com.elaniin.pokeapptest.FirebaseDataBase

import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry
import kotlinx.coroutines.flow.Flow

interface DataBaseImplementation {
    fun addPokemon(pokemonListEntry: PokemonListEntry)
    fun removePokemon(pokemonListEntry: PokemonListEntry)
    fun saveData(nameGroup: String, userId:String)
    fun removeData()
     fun retrieveData(userId: String,callback:(List<DataBasePokemon>)->Unit)
}