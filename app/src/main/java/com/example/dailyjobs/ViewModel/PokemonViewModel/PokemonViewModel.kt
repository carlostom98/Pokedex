package com.example.dailyjobs.ViewModel.PokemonViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyjobs.Model.GetPokemon
import com.example.dailyjobs.Model.PokemonDataModel.PokemonModel
import com.example.dailyjobs.Model.Services
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class PokemonViewModel() : ViewModel(), KoinComponent{
    private val getPokemon:GetPokemon = get()
    private var _pokemonInfo = MutableLiveData<PokemonModel>()
    val pokemonInfo: LiveData<PokemonModel> get() = _pokemonInfo
    fun getPokemonInfo(id: Int) {
        viewModelScope.launch {
            getPokemon.invoke(id)?.let {
                _pokemonInfo.postValue(it)
            }
        }
    }
}