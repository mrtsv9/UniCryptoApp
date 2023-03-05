package com.example.cryptoapp

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.example.cryptoapp.databinding.ActivityMainBinding
import com.example.cryptoapp.presentation.dialog.InternetFailedDialog
import androidx.navigation.ui.setupWithNavController
import com.example.cryptoapp.service.InternetService
import kotlin.also

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private var failedDialog: InternetFailedDialog? = null
    private var isAlreadyShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Intent(this, InternetService::class.java).also {
            startService(it)
        }

        LocalBroadcastManager.getInstance(application).registerReceiver(
            messageReceiver, IntentFilter("CheckInternet")
        )

        val bottomNavigationView = binding?.bottomNav
        val navController = findNavController(R.id.container)
        bottomNavigationView?.setupWithNavController(navController)

    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val message = intent.getStringExtra("Connection")
            if (message == "DISCONNECTED" && !isAlreadyShow) {
                isAlreadyShow = true
                showDialog()
            }
            else if (message == "CONNECTED" && isAlreadyShow) {
                isAlreadyShow = false
                hideDialog()
            }
        }
    }

    private fun showDialog() {
        failedDialog = InternetFailedDialog()
        failedDialog!!.show(supportFragmentManager, "MyCustomFragment")
    }

    private fun hideDialog() {
        failedDialog!!.dismiss()
    }

}