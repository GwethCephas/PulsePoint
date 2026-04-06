package com.ceph.pulsepoint

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.ceph.pulsepoint.di.appModule
import com.ceph.pulsepoint.di.coreModule
import com.ceph.pulsepoint.di.featureModule
import com.cephcoding.core.utils.Constants.PULSE_CHANNEL_ID
import com.cephcoding.core.utils.Constants.PULSE_CHANNEL_NAME
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PulsePointApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        startKoin {
            androidContext(this@PulsePointApp)
            modules(
                appModule,
                coreModule,
                featureModule
            )
        }

        val notificationChannel = NotificationChannel(
            PULSE_CHANNEL_ID,
            PULSE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}