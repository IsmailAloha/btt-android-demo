package com.bluetriangle.bluetriangledemo

import android.app.Application
import com.bluetriangle.analytics.Tracker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Tracker.init(applicationContext, SITE_ID)
        Tracker.instance?.trackCrashes()
    }
}