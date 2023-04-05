package com.example.dailyjobs.ViewModel.PokemonViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyjobs.Model.GetPokemon
import com.example.dailyjobs.Model.PokemonDataModel.PokemonModel
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private var _pokemonInfo = MutableLiveData<PokemonModel>()
    val pokemonInfo: LiveData<PokemonModel> get() = _pokemonInfo
    fun getPokemonInfo(id: Int) {
        viewModelScope.launch {
            val pokemon = GetPokemon(id).invoke()
            pokemon.let {
                _pokemonInfo.postValue(it)
            }
        }
    }
}