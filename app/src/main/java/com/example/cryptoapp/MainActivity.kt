package com.example.cryptoapp

import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.dialog.MyDialog
import com.example.cryptoapp.presentation.main_screen.MainFragment
import com.example.cryptoapp.presentation.main_screen.MainViewModel
import com.example.cryptoapp.test_service.MyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.Suppress
import kotlin.also

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Intent(this, MyService::class.java).also {
            startService(it)
        }

        LocalBroadcastManager.getInstance(application).registerReceiver(
            messageReceiver, IntentFilter("CheckInternet")
        )

    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val message = intent.getStringExtra("Connection")
            if (message == "DISCONNECTED") {
//                Log.d("QWE", "DISCONNECTED")
                showDialog()
            } else {
//                Log.d("QWE", "CONNECTED")
            }

        }
    }

//    private fun  exitListener() {
//        CoroutineScope(Dispatchers.IO).launch {
//            runBlocking {
//                val db = CryptoDatabase.getDatabase(applicationContext)
//                viewModel.insertCryptos(db)
//            }
//        }
//
//    }

    private fun showDialog() {
        MyDialog().show(supportFragmentManager, "MyCustomFragment")
    }

}