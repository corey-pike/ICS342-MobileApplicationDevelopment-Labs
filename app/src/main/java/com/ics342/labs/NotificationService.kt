package com.ics342.labs

import android.Manifest.permission
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class NotificationService : Service() {

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ContextCompat.checkSelfPermission(
                this@NotificationService,
                permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf()
            return START_NOT_STICKY
        }

        val smallIcon = R.drawable.star

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification Title")
            .setContentText("This is the notification content")
            .setSmallIcon(smallIcon)
            .setContentIntent(createNotificationIntent())
            .build()

        startForeground(NOTIFICATION_ID, notification)
        notificationManager.notify(NOTIFICATION_ID, notification)
        return START_STICKY_COMPATIBILITY
    }

    private fun createNotificationIntent(): PendingIntent {
        val notificationIntent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
    override fun onBind(intent: Intent?): IBinder? {
        // No need to implement for lab 8
        return null
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Labs8"
            val channelDescription = "Description for Labs8"
            val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = channelDescription
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "LABS8_CHANNEL_ID"
        private const val NOTIFICATION_ID = 1234
    }
}
