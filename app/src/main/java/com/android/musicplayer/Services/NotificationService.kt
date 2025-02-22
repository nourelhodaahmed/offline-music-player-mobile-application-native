package com.android.musicplayer.Services

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.session.MediaSession
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.musicplayer.R
import com.android.musicplayer.data.MediaManager
import com.android.musicplayer.ui.MainActivity
import com.android.musicplayer.util.Constants
import androidx.media.app.NotificationCompat.MediaStyle
import com.android.musicplayer.data.DataManager


class NotificationService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        //CREATE THE NOTIFICATION

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                Constants.SERVICE_CHANNEL_HELPER.CHANNEL_ID,
                Constants.SERVICE_CHANNEL_HELPER.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            val notification = Notification.Builder(
                this,
                Constants.SERVICE_CHANNEL_HELPER.CHANNEL_ID
            ).apply {
                    setContentTitle("Now Playing")
                    setContentText("song name")
                    setSmallIcon(R.drawable.music)
            }.build()

            startForeground(
                Constants.SERVICE_CHANNEL_HELPER.SERVICE_ID,
                notification
            )
        }
    }

}