package com.example.bitirmev2

import android.app.Application
import com.example.bitirmev2.notifications.NotificationHelper

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannel(this)
    }
}