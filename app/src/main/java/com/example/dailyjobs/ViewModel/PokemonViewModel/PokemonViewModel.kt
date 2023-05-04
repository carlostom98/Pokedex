package com.example.dailyjobs.ViewModel.PokemonViewModel

import android.content.IntentSender.OnFinished
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.dailyjobs.Model.GetPokemon
import com.example.dailyjobs.Model.GetPokemonList
import com.example.dailyjobs.Model.Pokemon2Model.PokedexProperties
import com.example.dailyjobs.Model.Pokemon2Model.PokemonList
import com.example.dailyjobs.Model.PokemonListModel.PokemonListEntry
import com.example.dailyjobs.Resource
import com.example.dailyjobs.Tools.Tools
import com.google.gson.annotations.Until
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.*


class PokemonViewModel() : ViewModel(), KoinComponent {
    private val getPokemon: GetPokemon = get()
    private val getPokemonList: GetPokemonList = get()
    private var _pokemonInfo = MutableLiveData<PokedexProperties>()
    val pokemonInfo: LiveData<PokedexProperties> get() = _pokemonInfo

    private var current_page = 0
    private var _pokemonList = MutableLiveData<List<PokemonListEntry>>()
    private var _is_succes = MutableLiveData<Boolean>()
    private var _is_loading = MutableLiveData<Boolean>()
    private var _is_error = MutableLiveData<String>()

    val pokemonList: LiveData<List<PokemonListEntry>> get() = _pokemonList
    val is_succes: LiveData<Boolean> get() = _is_succes
    val is_loading: LiveData<Boolean> get() = _is_loading
    val is_error: LiveData<String> get() = _is_error

    init {
        loadPaginatingPokemon()
    }

    fun getPokemonInfo(name: String) {
        viewModelScope.launch {
            _is_loading.postValue(true)
            getPokemon.invoke(name).let { pokeInfo ->
                when (pokeInfo) {
                    is Resource.Succes -> {
                        pokeInfo.data?.let { pokedex_information ->
                            _pokemonInfo.postValue(
                                pokedex_information
                            )
                        }
                    }
                    is Resource.Error -> {}
                }

            }
        }
    }

    fun loadPaginatingPokemon() {
        viewModelScope.launch {
            getPokemonList.invoke(Tools.PAGE_SIZE, current_page * Tools.PAGE_SIZE).let { pokeList ->
                when (pokeList) {
                    is Resource.Succes -> {
                        _is_succes.postValue(current_page * Tools.PAGE_SIZE >= pokeList.data!!.count)
                        pokeList.data.results.map { listData ->
                            val number = if (listData.url.endsWith("/")) {
                                listData.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                listData.url.takeLastWhile { it.isDigit() }
                            }.toInt()
                            val urlImage =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${number}.png"

                            PokemonListEntry(listData.name,urlImage, number)
                        }.let {
                            _pokemonList.postValue(it)
                        }
                    }
                    is Resource.Error -> {
                        _is_error.postValue(pokeList.message!!)
                        _is_loading.postValue(false)
                    }
                }
            }
        }
        current_page++
    }
}