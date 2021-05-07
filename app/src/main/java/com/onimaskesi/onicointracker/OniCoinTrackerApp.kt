package com.onimaskesi.onicointracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log

class OniCoinTrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val channel1 = NotificationChannel(
                    "channel_1",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH)

            channel1.description = "Daily notification"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel1)

        }
    }

}