package com.bluetriangle.bluetriangledemo

import android.app.Application
import com.bluetriangle.analytics.Tracker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApplication : Application() {
    companion object {
        lateinit var tinyDB: TinyDB
    }

    override fun onCreate() {
        super.onCreate()
        tinyDB = TinyDB(applicationContext)

//        val siteId = tinyDB.getString("BttSiteId")
//        if (!siteId.isNullOrBlank())
//            intTracker(siteId) //bluetriangledemo500z
    }

    fun intTracker(siteId: String?) {
        if (siteId.isNullOrBlank()) return

        Tracker.init(this, siteId)
        Tracker.instance?.trackCrashes()
    }
}