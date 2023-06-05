package com.elaniin.pokeapptest.ViewModel.PokemonViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elaniin.pokeapptest.FirebaseDataBase.DataBaseImplementation
import com.elaniin.pokeapptest.FirebaseDataBase.DataBasePokemon
import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry
import kotlinx.coroutines.launch

class DataBaseManagerViewModel(private val dbManager:DataBaseImplementation):ViewModel() {
    private val _pokemonFromDB= MutableLiveData<List<DataBasePokemon>>()
    val pokemonFromDB:LiveData<List<DataBasePokemon>> get() = _pokemonFromDB
    fun saveData(nameGroup: String){
        dbManager.saveData(nameGroup)
    }
    fun addPokemon(pokemon:PokemonListEntry){
        dbManager.addPokemon(pokemon)
    }
    fun getAllPokemonsInDB(){
        dbManager.retrieveData {
            it.let {
                _pokemonFromDB.postValue(it)
            }
        }
    }
    fun removePokemon(pokemon:PokemonListEntry){
        dbManager.removePokemon(pokemon)
    }
}