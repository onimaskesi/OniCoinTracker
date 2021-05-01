package com.onimaskesi.onicointracker.view

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.HmsMessaging
import com.onimaskesi.onicointracker.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.coin_recycler_raw.view.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(this, R.id.fragment)
        bottomNavigation.setupWithNavController(navController)

        val hmsMessaging = HmsMessaging.getInstance(this)
        hmsMessaging.isAutoInitEnabled = true
    }
}