package com.example.dailyjobs.Model

import com.example.dailyjobs.Tools.Tools
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
    single {
        Services(get<ApiService>())
    }
    single{
        GetPokemon(get<Services>())
    }
}
