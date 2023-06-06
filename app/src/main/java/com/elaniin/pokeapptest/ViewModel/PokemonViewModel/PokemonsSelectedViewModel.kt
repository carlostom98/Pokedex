package com.elaniin.pokeapptest.ViewModel.PokemonViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PokemonsSelectedViewModel : ViewModel() {
    private var NUMBER_OF_SELECTED_POKEMONS = 0
    private val _quantityAchieve = MutableLiveData(false)
    val quantityAchieve: LiveData<Boolean> get() = _quantityAchieve


    fun addPokemon() {
        NUMBER_OF_SELECTED_POKEMONS++
        checked()
    }

    fun removePokemon() {
        NUMBER_OF_SELECTED_POKEMONS--
        checked()
    }

    fun restartCount() {
        NUMBER_OF_SELECTED_POKEMONS = 0
    }


    private fun checked() {
        if (NUMBER_OF_SELECTED_POKEMONS in 3..5) {
            _quantityAchieve.postValue(true)
        } else {
            _quantityAchieve.postValue(false)
        }
        Log.d("NUMPER_OF_ADDED", "${NUMBER_OF_SELECTED_POKEMONS}")
    }
}