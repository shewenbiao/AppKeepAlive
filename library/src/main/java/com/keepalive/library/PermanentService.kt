package com.keepalive.library

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.keepalive.library.util.Utils

/**
 * @author : She Wenbiao
 * @date   : 2021/4/18 12:40 AM
 */
open class PermanentService : Service() {

    companion object {

        private const val NOTIFICATION_ID = 0x11
        private const val SERVICE_FOREGROUND_NOTIFICATION_ID = 100001
        private const val EXTRA_NOTIFICATION_CONFIG = "notification_config"

        fun start(context: Context, foregroundNotificationConfig: ForegroundNotificationConfig?) {
            if (!Utils.isServiceRunning(context, PermanentService::class.java)) {
                val intent = Intent(context, PermanentService::class.java)
                intent.putExtra(EXTRA_NOTIFICATION_CONFIG, foregroundNotificationConfig)
                try {
                    context.startService(intent)
                } catch (e: Exception) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            context.startForegroundService(intent)
                        } catch (e1: Exception) {
                        }
                    }
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getDefaultNotificationConfig(context: Context) =
            ForegroundNotificationConfig(
                SERVICE_FOREGROUND_NOTIFICATION_ID,
                getForegroundNotification(context)
            )

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getForegroundNotification(context: Context): Notification {
            val channelId = "permanent"
            val channelName = "Permanent"
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification_messages)
                .setOngoing(true)
                .setWhen(0)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setAutoCancel(false)
            return builder.build()
        }

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sdkInt = Build.VERSION.SDK_INT
        when {
            sdkInt >= Build.VERSION_CODES.O -> {
                var foregroundNotificationConfig: ForegroundNotificationConfig?= null
                if (intent != null) {
                    foregroundNotificationConfig = intent.getParcelableExtra(EXTRA_NOTIFICATION_CONFIG)
                }

                if (foregroundNotificationConfig == null) {
                    foregroundNotificationConfig = getDefaultNotificationConfig(this)
                }
                startForeground(foregroundNotificationConfig)
            }
            sdkInt >= Build.VERSION_CODES.N_MR1 -> try {
                stopForeground(true)
            } catch (e: java.lang.Exception) {
            }
            sdkInt >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> {
                //API 18以上，发送Notification并将其置为前台后，启动InnerService
                val builder = Notification.Builder(this)
                builder.setSmallIcon(R.drawable.ic_notification_messages)
                startForeground(NOTIFICATION_ID, builder.build())
                startService(Intent(this, InnerService::class.java))
            }
            else ->
                //API 18以下，直接发送Notification并将其置为前台
                startForeground(NOTIFICATION_ID, Notification())
        }
        return START_REDELIVER_INTENT
    }

    private fun startForeground(foregroundNotificationConfig: ForegroundNotificationConfig) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(foregroundNotificationConfig.id, foregroundNotificationConfig.notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            stopForeground(true)
        } catch (e: java.lang.Exception) {
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            start(applicationContext, null)
        }
    }

    class InnerService : Service() {
        override fun onBind(intent: Intent?): IBinder? = null

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            try {
                //发送ID相同的Notification，然后将其取消并取消自己的前台显示
                val builder = Notification.Builder(this)
                builder.setSmallIcon(R.drawable.ic_notification_messages)
                startForeground(NOTIFICATION_ID, builder.build())
                Handler().postDelayed(Runnable {
                    stopForeground(true)
                    val notificationManager: NotificationManager = getSystemService(
                        NOTIFICATION_SERVICE
                    ) as NotificationManager
                    notificationManager.cancel(NOTIFICATION_ID)
                    stopSelf()
                }, 100)
            } catch (e: Exception) {

            }
            return START_NOT_STICKY
        }

    }
}