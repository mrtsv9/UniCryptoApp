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
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.dialog.MyDialog
import com.example.cryptoapp.presentation.main_screen.MainFragment
import com.example.cryptoapp.presentation.main_screen.MainFragmentDirections
import com.example.cryptoapp.presentation.main_screen.MainViewModel
import com.example.cryptoapp.test_service.MyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cryptoapp.presentation.details_screen.DetailsFragment
import com.example.cryptoapp.presentation.settings_screen.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun showDialog() {
        MyDialog().show(supportFragmentManager, "MyCustomFragment")
    }

}