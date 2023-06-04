package com.elaniin.pokeapptest

import android.app.Application
import com.elaniin.pokeapptest.Model.diModules.moduleVM
import com.elaniin.pokeapptest.Model.appModule
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