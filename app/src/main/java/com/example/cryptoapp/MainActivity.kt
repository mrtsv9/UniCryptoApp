package com.example.cryptoapp

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.presentation.dialog.MyDialog
import com.example.cryptoapp.presentation.main_screen.MainViewModel
import com.example.cryptoapp.service.MyService
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.Suppress
import kotlin.also

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Intent(this, MyService::class.java).also {
            startService(it)
        }

        LocalBroadcastManager.getInstance(application).registerReceiver(
            messageReceiver, IntentFilter("CheckInternet")
        )

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navController = findNavController(R.id.container)
        bottomNavigationView.setupWithNavController(navController)

//        binding?.bottomMenu?.setOnItemSelectedListener  {
//            when(it.itemId) {
//                R.id.miHome -> {
//                    loadFragment(MainFragment())
//                    true
//                }
//                R.id.miSettings -> {
//                    loadFragment(SettingsFragment())
//                    true
//                }
//                else -> false
//            }
//        }

    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val message = intent.getStringExtra("Connection")
            if (message == "DISCONNECTED") {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        MyDialog().show(supportFragmentManager, "MyCustomFragment")
    }

}