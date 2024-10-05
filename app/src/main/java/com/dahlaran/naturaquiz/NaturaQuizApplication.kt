package com.dahlaran.naturaquiz

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class NaturaQuizApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}