package com.bluetriangle.bluetriangledemo

import android.app.Application
import android.util.Log
import com.bluetriangle.analytics.BlueTriangleConfiguration
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.tests.HeavyLoopTest
import com.bluetriangle.bluetriangledemo.tests.MemoryMonitor
import com.bluetriangle.bluetriangledemo.utils.generateDemoWebsiteFromTemplate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApplication : Application() {

    var memoryMonitor = MemoryMonitor()

    companion object {
        lateinit var tinyDB: TinyDB
        const val TAG_URL = "TAG_URL"
        const val DEFAULT_TAG_URL = "$DEFAULT_SITE_ID.btttag.com/btt.js"

        private var demoWebsiteUrl = ""
        val DEMO_WEBSITE_URL
            get() = demoWebsiteUrl

        fun checkAndRunLaunchScenario(scenario: Int) {
            val launchTest = tinyDB.getBoolean(KEY_LAUNCH_TEST)
            val launchTestScenario = tinyDB.getInt(KEY_LAUNCH_SCENARIO, 1)
            Log.d(
                "DemoApplication",
                "checkAndRunLaunchScenario: isLaunchTest: $launchTest, Scenario: $launchTestScenario"
            )
            if (launchTest && launchTestScenario == scenario) {
                tinyDB.remove(KEY_LAUNCH_TEST)
                tinyDB.remove(KEY_LAUNCH_SCENARIO)
                if (scenario == SCENARIO_APP_CREATE) {
                    tinyDB.setBoolean(KEY_SHOULD_NOT_SHOW_CONFIGURATION, true)
                }
                HeavyLoopTest(3L).run()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        tinyDB = TinyDB(applicationContext)

        val launchTest = tinyDB.getBoolean(KEY_LAUNCH_TEST)
        Log.d("DemoApplication", "Application.onCreate: Launch Test: $launchTest")
        val siteId = tinyDB.getString(KEY_SITE_ID, DEFAULT_SITE_ID)
        val anrDetection = tinyDB.getBoolean(KEY_ANR_ENABLED, true)
        val screenTracking = tinyDB.getBoolean(KEY_SCREEN_TRACKING_ENABLED, true)

        initTracker(siteId, anrDetection, screenTracking)

        demoWebsiteUrl = "file://${filesDir.absolutePath}/index.html"

        if(!hasTagUrl()) {
            setTagUrl(DEFAULT_TAG_URL)
        }
        checkAndRunLaunchScenario(SCENARIO_APP_CREATE)

        Thread(memoryMonitor).start()
    }

    private fun initTracker(siteId: String?, anrDetection: Boolean, screenTracking: Boolean) {
        if (siteId.isNullOrBlank()) return

        val configuration = BlueTriangleConfiguration()
        configuration.siteId = siteId
        configuration.isTrackAnrEnabled = anrDetection
        configuration.isScreenTrackingEnabled = screenTracking
        configuration.isLaunchTimeEnabled = true
        configuration.isPerformanceMonitorEnabled = true
        configuration.networkSampleRate = 1.0
//        configuration.cacheMemoryLimit = 10 * 1000L
        configuration.cacheExpiryDuration = 120 * 1000L
        configuration.isMemoryWarningEnabled = true
        configuration.isTrackNetworkStateEnabled = true
        Tracker.init(this, configuration)
        Tracker.instance?.trackCrashes()
    }

    fun hasTagUrl(): Boolean {
        return tinyDB.contains(TAG_URL)
    }

    fun getTagUrl(): String {
        return tinyDB.getString(TAG_URL, DEFAULT_TAG_URL) ?: DEFAULT_TAG_URL
    }

    fun setTagUrl(url: String) {
        tinyDB.setString(TAG_URL, url)
        generateDemoWebsiteFromTemplate()
    }
}