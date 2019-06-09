package com.dew.easeeye

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.dew.easeeye.Utility.CHANNEL_ID

/**
 * Created by Edward on 6/9/2019.
 */

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()
    }

    // this creating notification channel can be called in any where in program
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  // since Android O version, service must have a notification channel.

            val channel = NotificationChannel(
                CHANNEL_ID,
                "EaseEye",
                NotificationManager.IMPORTANCE_LOW
            ).apply {  // more kotlin way
                description = "This is Easy Eye Channel"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}