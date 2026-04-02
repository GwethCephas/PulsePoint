package com.ceph.pulsepoint

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.ceph.pulsepoint.di.databaseModule
import com.ceph.pulsepoint.di.networkModule
import com.ceph.pulsepoint.di.repositoryModule
import com.ceph.pulsepoint.di.viewModelModule
import com.ceph.pulsepoint.di.workerModule
import com.ceph.pulsepoint.utils.Constants.PULSE_CHANNEL_ID
import com.ceph.pulsepoint.utils.Constants.PULSE_CHANNEL_NAME
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PulsePointApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PulsePointApp)
            modules(viewModelModule, repositoryModule, networkModule, databaseModule, workerModule)
        }

        val notificationChannel = NotificationChannel(
            PULSE_CHANNEL_ID,
            PULSE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}