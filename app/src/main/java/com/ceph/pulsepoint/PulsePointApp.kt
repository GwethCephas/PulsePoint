package com.ceph.pulsepoint

import android.app.Application
import com.ceph.pulsepoint.di.appModule
import com.ceph.pulsepoint.di.coreModule
import com.ceph.pulsepoint.di.featureModule
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
    }
}