package com.example.cryptoapp.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
//            modules(listOf(appModule, viewModelModule))
            modules(listOf(appModule, viewModelModule))
        }
    }

}