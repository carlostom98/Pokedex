package com.example.dailyjobs.ViewModel.PokemonViewModel

import android.content.IntentSender.OnFinished
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette

class ColorBackGroundViewModel:ViewModel() {

    private var _newColor= MutableLiveData<Color>()
    val newColor:LiveData<Color> get() = _newColor

    fun calculateDominantColor(drawable:Drawable){
        val bmp=( drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette->
            palette?.dominantSwatch?.rgb.let { dominantColor->
                _newColor.postValue(Color(dominantColor!!))
            }
        }
    }
}