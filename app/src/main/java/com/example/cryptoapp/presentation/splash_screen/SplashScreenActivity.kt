package com.example.cryptoapp.presentation.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.domain.data_source.CryptoRemoteDataSource
import com.example.cryptoapp.presentation.main_screen.MainRepository
import com.example.cryptoapp.presentation.main_screen.MainViewModel
//import com.example.cryptoapp.domain.service.CryptoRemoteDataSource
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        CoroutineScope(Dispatchers.IO).launch {
            val db = CryptoDatabase.getDatabase(applicationContext)
            viewModel.insertCryptos(db)
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}