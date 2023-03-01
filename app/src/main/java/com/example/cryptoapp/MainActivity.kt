package com.example.cryptoapp

import android.app.StatusBarManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.di.appModule
import com.example.cryptoapp.presentation.main_screen.MainFragment
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, MainFragment())
            .commit()
    }
}