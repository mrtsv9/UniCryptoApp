package com.example.cryptoapp.presentation.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.cryptoapp.MainActivity
import com.example.cryptoapp.R
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.databinding.ActivitySplashScreenBinding
import com.example.cryptoapp.presentation.contracts.SplashContract
import com.example.cryptoapp.presentation.main_screen.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.system.measureTimeMillis

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()
    private var binding: ActivitySplashScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        initObservers()

        val image = binding?.ivSplashImage
        val myVectorIcon = AnimatedVectorDrawableCompat.create(
            this,
            R.drawable.animated_splash
        )

        image?.setImageDrawable(myVectorIcon)
        myVectorIcon?.start()

        viewModel.insertCryptos()

    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.effect.collect {
                when(it) {
                    is SplashContract.Effect.OpenMainActivity -> {
                        openMainActivity()
                    }
                }
            }
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}






