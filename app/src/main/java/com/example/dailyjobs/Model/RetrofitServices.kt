package com.example.dailyjobs.Model

import com.example.dailyjobs.Tools.Tools
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServices {
    // Pokemon Api Services
    fun pokemonRetrofit():Retrofit {
        return Retrofit.Builder()
            .baseUrl(Tools.urlPokemonApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}