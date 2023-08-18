package com.bluetriangle.bluetriangledemo

import android.app.Application
import android.util.Log
import com.bluetriangle.analytics.BlueTriangleConfiguration
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.android.demo.tests.HeavyLoopTest
import dagger.hilt.android.HiltAndroidApp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@HiltAndroidApp
class DemoApplication : Application() {
    companion object {
        lateinit var tinyDB: TinyDB

        fun checkAndRunLaunchScenario(scenario:Int) {
            val launchTest = tinyDB.getBoolean(KEY_LAUNCH_TEST)
            val launchTestScenario = tinyDB.getInt(KEY_LAUNCH_SCENARIO, 1)
            Log.d("DemoApplication", "checkAndRunLaunchScenario: isLaunchTest: $launchTest, Scenario: $launchTestScenario")
            if(launchTest && launchTestScenario == scenario) {
                tinyDB.remove(KEY_LAUNCH_TEST)
                tinyDB.remove(KEY_LAUNCH_SCENARIO)
                if(scenario == SCENARIO_APP_CREATE) {
                    tinyDB.setBoolean(KEY_SHOULD_NOT_SHOW_CONFIGURATION, true)
                }
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
        Log.d("DemoApplication", "Application.onCreate: Launch Test: $launchTest")
        val siteId = tinyDB.getString(KEY_SITE_ID, DEFAULT_SITE_ID)
        val anrDetection = tinyDB.getBoolean(KEY_ANR_ENABLED, true)
        val screenTracking = tinyDB.getBoolean(KEY_SCREEN_TRACKING_ENABLED, true)
        val formatter = SimpleDateFormat("ddMMyyyykkmm", Locale.getDefault())
        val sessionId = formatter.format(Calendar.getInstance().time)
        Log.d("BlueTriangle", "Session ID: ${sessionId}")

        initTracker(siteId, anrDetection, screenTracking, sessionId)

        checkAndRunLaunchScenario(SCENARIO_APP_CREATE)

    }

    fun initTracker(siteId: String?, anrDetection: Boolean, screenTracking: Boolean, sessionId: String) {
        if (siteId.isNullOrBlank()) return

        val configuration = BlueTriangleConfiguration()
        configuration.siteId = siteId
        configuration.isTrackAnrEnabled = anrDetection
        configuration.isScreenTrackingEnabled = screenTracking
        configuration.isLaunchTimeEnabled = true
        configuration.isPerformanceMonitorEnabled = true
        configuration.networkSampleRate = 1.0
        configuration.sessionId = sessionId
        Tracker.init(this, configuration)
        Tracker.instance?.trackCrashes()
    }
}