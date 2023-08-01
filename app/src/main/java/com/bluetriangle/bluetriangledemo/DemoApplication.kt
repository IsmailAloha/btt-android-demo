package com.bluetriangle.bluetriangledemo

import android.app.Application
import com.bluetriangle.analytics.BlueTriangleConfiguration
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.android.demo.tests.HeavyLoopTest
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApplication : Application() {
    companion object {
        lateinit var tinyDB: TinyDB

        fun checkAndRunLaunchScenario(scenario:Int) {
            val launchTestScenario = tinyDB.getInt(KEY_LAUNCH_SCENARIO, 1)
            if(launchTestScenario == scenario) {
                tinyDB.remove(KEY_LAUNCH_TEST)
                tinyDB.remove(KEY_LAUNCH_SCENARIO)
                HeavyLoopTest(3L).run()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        tinyDB = TinyDB(applicationContext)

//        val siteId = tinyDB.getString("BttSiteId")
//        if (!siteId.isNullOrBlank())
//            intTracker(siteId) //bluetriangledemo500z
        val launchTest = tinyDB.getBoolean(KEY_LAUNCH_TEST)
        if(launchTest) {
            val siteId = tinyDB.getString(KEY_SITE_ID)
            val anrDetection = tinyDB.getBoolean(KEY_ANR_ENABLED)
            val screenTracking = tinyDB.getBoolean(KEY_SCREEN_TRACKING_ENABLED)

            initTracker(siteId, anrDetection, screenTracking)

            checkAndRunLaunchScenario(SCENARIO_APP_CREATE)
        }

    }

    fun initTracker(siteId: String?, anrDetection: Boolean, screenTracking: Boolean) {
        if (siteId.isNullOrBlank()) return

        val configuration = BlueTriangleConfiguration()
        configuration.siteId = siteId
        configuration.isTrackAnrEnabled = anrDetection
        configuration.isScreenTrackingEnabled = screenTracking
        configuration.isLaunchTimeEnabled = true
        configuration.isPerformanceMonitorEnabled = true
        Tracker.init(this, configuration)
        Tracker.instance?.trackCrashes()
    }
}