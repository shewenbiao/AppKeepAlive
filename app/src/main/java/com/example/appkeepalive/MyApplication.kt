package com.example.appkeepalive

import android.app.*
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.keepalive.library.KeepAliveManager
import com.keepalive.library.ForegroundNotificationConfig

/**
 * @author : She Wenbiao
 * @date   : 2021/4/18 2:22 PM
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        var notification: Notification? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = getForegroundNotification()
        }
        KeepAliveManager.start(
            this,
            ForegroundNotificationConfig(getForegroundNotificationId(), notification)
        )
    }

    private fun getForegroundNotificationId() = 1000

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getForegroundNotification(): Notification {
        val channelId = "custom"
        val channelName = "custom"
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        val remoteViews = RemoteViews(packageName, R.layout.foreground_notification)
        val intent = Intent(this, MainActivity::class.java)
        remoteViews.setOnClickPendingIntent(
            R.id.root,
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        val builder = NotificationCompat.Builder(this, channelId)
        builder.setContent(remoteViews)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setWhen(0)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(false)
        return builder.build()
    }
}