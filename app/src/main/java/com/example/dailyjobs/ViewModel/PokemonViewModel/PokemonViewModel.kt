package com.example.dailyjobs.ViewModel.PokemonViewModel

import android.content.IntentSender.OnFinished
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.dailyjobs.Model.GetPokemon
import com.example.dailyjobs.Model.Pokemon2Model.PokedexProperties
import com.google.gson.annotations.Until
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class PokemonViewModel() : ViewModel(), KoinComponent{
    private val getPokemon:GetPokemon = get()
    var _pokemonInfo = MutableLiveData<PokedexProperties>()
    val pokemonInfo : LiveData<PokedexProperties> get() = _pokemonInfo

    fun calculateDominantColor(drawable:Drawable, onFinished: (Color)->Unit){
        val bmp= (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette->
            palette?.dominantSwatch?.rgb.let { colorValue->
                onFinished(Color(colorValue!!))
            }
        }
    }

    fun getPokemonInfo(id: Int) {
        viewModelScope.launch {
            getPokemon.invoke(id).let { it ->
                it.data?.let {pokedex_information-> _pokemonInfo.postValue(pokedex_information) }
            }
        }
    }
}