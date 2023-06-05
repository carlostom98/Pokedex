package com.elaniin.pokeapptest.ViewModel.PokemonViewModel

import androidx.lifecycle.ViewModel
import com.elaniin.pokeapptest.FirebaseDataBase.DataBaseManager
import com.elaniin.pokeapptest.FirebaseDataBase.DataBasePokemon

class DataBaseManagerViewModel(private val dbManager:DataBaseManager):ViewModel() {
    fun saveData(dataBasePokemon: DataBasePokemon){
        dbManager.saveData(dataBasePokemon)
    }
}