package com.amoseui.camerainfo

import android.app.Application
import timber.log.Timber

class CameraInfoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}