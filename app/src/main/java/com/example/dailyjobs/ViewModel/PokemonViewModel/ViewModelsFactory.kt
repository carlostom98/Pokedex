package com.example.dailyjobs.ViewModel.PokemonViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object ViewModelsFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            return PokemonViewModel() as T
        }
        if(modelClass.isAssignableFrom(ColorBackGroundViewModel::class.java)){
            return ColorBackGroundViewModel() as T
        }
        throw java.lang.IllegalArgumentException("Invalid Paramether")
    }
}