package com.example.dailyjobs.Model

import com.example.dailyjobs.Tools.Tools
import com.example.dailyjobs.ViewModel.PokemonViewModel.PokemonViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(Tools.urlPokemonApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    single<Services> {
        Services(get())
    }
}