package com.elaniin.pokeapptest.FirebaseDataBase

import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry
import com.google.firebase.database.FirebaseDatabase

class DataBaseRTDB:DataBaseImplementation {
    private val database=FirebaseDatabase.getInstance()


    override fun addPokemon(pokemonListEntry: PokemonListEntry) {
        TODO("Not yet implemented")
    }

    override fun removePokemon(pokemonListEntry: PokemonListEntry) {
        TODO("Not yet implemented")
    }

    override fun saveData(nameGroup: String) {
        TODO("Not yet implemented")
    }


    override fun removeData() {

    }

    override fun retrieveData() {

    }

}