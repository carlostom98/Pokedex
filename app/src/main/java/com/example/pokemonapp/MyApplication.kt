package com.example.pokemonapp

import android.app.Application
import com.example.pokemonapp.Model.diModules.moduleVM
import com.example.pokemonapp.Model.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule, moduleVM)
        }
    }
}