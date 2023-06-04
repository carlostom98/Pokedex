package com.elaniin.pokeapptest.ViewModel.PokemonViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elaniin.pokeapptest.Model.GetPokemon
import com.elaniin.pokeapptest.Model.GetPokemonList
import com.elaniin.pokeapptest.Model.Pokemon2Model.PokedexProperties
import com.elaniin.pokeapptest.Model.PokemonListModel.PokemonListEntry
import com.elaniin.pokeapptest.Resource
import com.elaniin.pokeapptest.Tools.Tools
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class PokemonViewModel() : ViewModel(), KoinComponent {
    private val getPokemon: GetPokemon = get()
    private val getPokemonList: GetPokemonList = get()
    private var _pokemonInfo = MutableLiveData<PokedexProperties>()
    val pokemonInfo: LiveData<PokedexProperties> get() = _pokemonInfo

    private var _pokemonList = MutableLiveData<List<PokemonListEntry>>()
    private var _is_succes = MutableLiveData<Boolean>()
    private var _is_loading = MutableLiveData<Boolean>()
    private var _is_error = MutableLiveData<String>()

    val pokemonList: LiveData<List<PokemonListEntry>> get() = _pokemonList
    val is_succes: LiveData<Boolean> get() = _is_succes
    val is_loading: LiveData<Boolean> get() = _is_loading
    val is_error: LiveData<String> get() = _is_error

    init {
        loadPaginatingPokemon(0)
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
                    is Resource.Loading->{}
                }
            }
        }
    }

    fun loadPaginatingPokemon(pageNumber: Int) {
        viewModelScope.launch {
            getPokemonList.invoke(Tools.PAGE_SIZE, (pageNumber) * Tools.PAGE_SIZE).let { pokeList ->
                when (pokeList) {
                    is Resource.Succes -> {
                        _is_succes.postValue(pageNumber * Tools.PAGE_SIZE >= pokeList.data!!.count)
                        pokeList.data.results.map { listData ->
                            val number = if (listData.url.endsWith("/")) {
                                listData.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                listData.url.takeLastWhile { it.isDigit() }
                            }.toInt()
                            val urlImage =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${number}.png"

                            PokemonListEntry(listData.name, urlImage, number)
                        }.let {
                            _pokemonList.postValue(it)
                            _is_loading.postValue(false)
                        }
                    }
                    is Resource.Error -> {
                        _is_error.postValue(pokeList.message!!)
                        _is_loading.postValue(false)
                    }
                    is Resource.Loading->{}
                }
            }
        }
    }
}