package com.example.bitirmev2.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bitirmev2.R
import java.util.*

object NotificationHelper {
    private const val CHANNEL_ID = "help_messages"
    private const val CHANNEL_NAME = "Yardım Mesajları"
    private const val GROUP_KEY = "help_message_group"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Yeni yardım mesajları ve cihaz bağlantı bildirimleri"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showSingleMessageNotification(context: Context, sender: String, type: String, address: String) {
        if (!hasNotificationPermission(context)) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Yeni Yardım Mesajı")
            .setContentText("📍 $type yardımı - $sender ($address)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(GROUP_KEY)
            .build()

        val id = UUID.randomUUID().hashCode()

        try {
            NotificationManagerCompat.from(context).notify(id, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun showGroupedSummary(context: Context, count: Int) {
        if (!hasNotificationPermission(context)) return

        val summary = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Toplu Yardım Mesajı")
            .setContentText("$count yeni yardım mesajı alındı")
            .setStyle(NotificationCompat.InboxStyle().setSummaryText("Yardım mesajları"))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup(GROUP_KEY)
            .setGroupSummary(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(0, summary)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun showConnectionStatus(context: Context, statusMessage: String) {
        if (!hasNotificationPermission(context)) return

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Cihaz Bağlantı Durumu")
            .setContentText(statusMessage)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        val id = UUID.randomUUID().hashCode()

        try {
            NotificationManagerCompat.from(context).notify(id, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }
}
