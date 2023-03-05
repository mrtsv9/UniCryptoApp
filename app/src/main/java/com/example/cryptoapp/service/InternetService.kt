package com.example.cryptoapp.service

import android.content.Intent
import android.os.*
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class InternetService : LifecycleService() {

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)


        NetworkStatusHelper(applicationContext).observe(this) {
            when (it) {
                NetworkStatus.Available -> {
                    sendMessageToActivity("CONNECTED")
                }
                NetworkStatus.Unavailable -> {
                    sendMessageToActivity("DISCONNECTED")
                }
            }
        }

        return START_STICKY
    }

    private fun sendMessageToActivity(msg: String) {
        val intent = Intent("CheckInternet")
        intent.putExtra("Connection", msg)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

}