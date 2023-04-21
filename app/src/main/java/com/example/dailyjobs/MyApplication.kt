package com.example.dailyjobs

import android.app.Application
import com.example.dailyjobs.Model.diModules.moduleVM
import com.example.dailyjobs.Model.appModule
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