package com.bluetriangle.bluetriangledemo

import android.app.Application
import com.bluetriangle.analytics.Tracker

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Tracker.init(applicationContext, SITE_ID)
        Tracker.getInstance().trackCrashes()
    }
}