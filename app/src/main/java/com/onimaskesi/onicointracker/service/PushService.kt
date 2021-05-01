package com.onimaskesi.onicointracker.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.onimaskesi.onicointracker.R
import com.onimaskesi.onicointracker.view.MainActivity

class PushService : HmsMessageService() {

    private val TAG = "PushService"

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.i(TAG, "onNewToken: $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        p0?.let {

            val data = it.dataOfMap
            val channelId = data.get("channel_id") as String

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val notification = NotificationCompat
                    .Builder(this, channelId)
                    .setContentTitle("The Coins Caught Fire! \uD83D\uDD25\uD83D\uDCB8\uD83D\uDD25")
                    .setContentText("Do you want to see the Coins \uD83E\uDD11\uD83E\uDD11")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_baseline_monetization_on_24)
                    .build()

            NotificationManagerCompat.from(this).notify(1, notification)

        }


    }

}