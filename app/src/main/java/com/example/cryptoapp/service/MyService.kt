package com.example.cryptoapp.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MyService(): Service() {

    private lateinit var handler: Handler
    private var status = "CONNECTED"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("QWE", "Service started")

        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {

                if(checkForInternet(applicationContext)) {
                    sendMessageToActivity("CONNECTED")
                    status = "CONNECTED"
//                    Log.d("QWE", "CONNECTED")
                }
                else if(status != "DISCONNECTED") {
                    sendMessageToActivity("DISCONNECTED")
                    status = "DISCONNECTED"
//                    Log.d("QWE", "DISCONNECTED")
                }

                super.handleMessage(msg)
            }
        }

        Thread {
            while (true) {
                try {
                    Thread.sleep(13000)
                    handler.sendEmptyMessage(0)
                } catch (e: InterruptedException) {
//                    Log.d("QWE", e.message.toString())
                }
            }
        }.start()

        return START_STICKY
    }

    private fun sendMessageToActivity(msg: String) {
        val intent = Intent("CheckInternet")
        intent.putExtra("Connection", msg)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("QWE", "Service destroyed")
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}