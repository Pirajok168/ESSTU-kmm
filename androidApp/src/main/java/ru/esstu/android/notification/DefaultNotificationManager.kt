package ru.esstu.android.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import ru.esstu.android.R
import ru.esstu.android.domain.ui.MainActivity
import kotlin.random.Random

class DefaultNotificationManager {

    private fun initNotificationBuilder(
        context: Context,
        notificationManager: NotificationManager
    ): NotificationCompat.Builder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (notificationChannel == null) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_DESCRIPTION, importance)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            NotificationCompat.Builder(context)
        }




    fun pushNotification(context: Context, id: Int, title: String, body: String? = null, deepLink: String? = null) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = initNotificationBuilder(context, notificationManager)

        notificationBuilder.setSmallIcon(R.drawable.ic_notification_logo)
        notificationBuilder.setContentTitle(title)
        body?.let {
            notificationBuilder.setContentText(body)
        }
        notificationBuilder.setContentIntent(
            TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(
                    Intent(
                        Intent.ACTION_VIEW,
                        deepLink?.toUri(),
                        context,
                        MainActivity::class.java
                    )
                )
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }
        )
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH

        val notificationId = Random(System.currentTimeMillis()).nextInt(10000)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {

        private const val CHANNEL_ID = "esstu_default_channel_id"
        private const val CHANNEL_DESCRIPTION = "ESSTU Default Channel"
    }
}