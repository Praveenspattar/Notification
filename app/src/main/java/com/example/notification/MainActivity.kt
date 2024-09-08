package com.example.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var notificationBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        notificationBtn = findViewById(R.id.notification_btn)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101
                )
            }
        }

        notificationBtn.setOnClickListener {
            makeNotification()
        }

    }

    fun makeNotification() {
        val channelId = "CHANNEL_ID"
        val builder = NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Notification title")
            .setContentText("notification Description")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val intent = Intent(applicationContext, NotificationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("data", "Notification Data")
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext,0,intent, PendingIntent.FLAG_MUTABLE)
        builder.setContentIntent(pendingIntent)

        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notificationChannel = notifManager.getNotificationChannel(channelId)

        if (notificationChannel == null) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            notificationChannel = NotificationChannel(channelId,
                "Some description ",importance).apply {
                    lightColor = Color.GREEN
                enableLights(true)
            }
            notifManager.createNotificationChannel(notificationChannel)
        }

        notifManager.notify(0,builder.build())

    }

}