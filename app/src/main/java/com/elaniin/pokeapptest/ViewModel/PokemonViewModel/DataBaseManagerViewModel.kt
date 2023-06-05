package com.elaniin.pokeapptest.ViewModel.PokemonViewModel

import androidx.lifecycle.ViewModel
import com.elaniin.pokeapptest.FirebaseDataBase.DataBaseImplementation
import com.elaniin.pokeapptest.FirebaseDataBase.DataBasePokemon
import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry

class DataBaseManagerViewModel(private val dbManager:DataBaseImplementation):ViewModel() {
    fun saveData(nameGroup: String){
        dbManager.saveData(nameGroup)
    }
    fun addPokemon(pokemon:PokemonListEntry){
        dbManager.addPokemon(pokemon)
    }
    fun removePokemon(pokemon:PokemonListEntry){
        dbManager.removePokemon(pokemon)
    }
}