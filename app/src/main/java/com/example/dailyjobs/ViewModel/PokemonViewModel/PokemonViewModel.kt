package com.example.dailyjobs.ViewModel.PokemonViewModel

import android.content.IntentSender.OnFinished
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.dailyjobs.Model.GetPokemon
import com.example.dailyjobs.Model.GetPokemonList
import com.example.dailyjobs.Model.Pokemon2Model.PokedexProperties
import com.example.dailyjobs.Resource
import com.example.dailyjobs.Tools.Tools
import com.google.gson.annotations.Until
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class PokemonViewModel() : ViewModel(), KoinComponent {
    private val getPokemon: GetPokemon = get()
    private val getPokemonList: GetPokemonList = get()
    var _pokemonInfo = MutableLiveData<PokedexProperties>()
    val pokemonInfo: LiveData<PokedexProperties> get() = _pokemonInfo

    private var current_page = 0
    private var is_succes = mutableStateOf(false)
    private var is_loading = mutableStateOf(false)
    private var is_error = mutableStateOf("")

    fun calculateDominantColor(drawable: Drawable, onFinished: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb.let { colorValue ->
                onFinished(Color(colorValue!!))
            }
        }
    }

    fun getPokemonInfo(id: Int) {
        viewModelScope.launch {
            is_loading.value = true
            getPokemon.invoke(id).let { pokeInfo ->
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
                        is_succes.value= current_page*Tools.PAGE_SIZE >= pokeList.data!!.count
                        val pokedekEntries= pokeList.data!!.results.mapIndexed { index, entry ->
                            val number= if (entry.url.endsWith("/")){
                                entry.url.drop(1).takeLastWhile { it.isDigit() }
                            }else{

                            }

                        }
                    }
                    is Resource.Error -> {}
                }
            }
        }
    }
}