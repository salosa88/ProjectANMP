package com.ubaya.projectanmp.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ubaya.projectanmp.R

object Notif {

    fun createChannel(
        ctx: Context,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        showBadge: Boolean = false,
        name: String = "Transaksi",
        description: String = "Notifikasi Expense Tracker"
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${ctx.packageName}-$name"
            val channel   = NotificationChannel(channelId, name, importance).apply {
                this.description = description
                setShowBadge(showBadge)
            }
            ctx.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    fun show(ctx: Context, title: String, content: String) {
        val channelId = "${ctx.packageName}-Transaksi"
        val builder   = NotificationCompat.Builder(ctx, channelId)
            .setSmallIcon(R.drawable.baseline_monetization_on_24)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val manager = NotificationManagerCompat.from(ctx)

        if (ActivityCompat.checkSelfPermission(
                ctx, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            manager.notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}