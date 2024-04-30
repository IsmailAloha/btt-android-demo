package com.bluetriangle.bluetriangledemo

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import com.bluetriangle.analytics.BlueTriangleConfiguration
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.layout.LauncherActivity
import com.bluetriangle.bluetriangledemo.tests.HeavyLoopTest
import com.bluetriangle.bluetriangledemo.tests.MemoryMonitor
import com.bluetriangle.bluetriangledemo.utils.DEFAULT_SITE_ID
import com.bluetriangle.bluetriangledemo.utils.KEY_LAUNCH_SCENARIO
import com.bluetriangle.bluetriangledemo.utils.KEY_LAUNCH_TEST
import com.bluetriangle.bluetriangledemo.utils.KEY_SHOULD_NOT_SHOW_CONFIGURATION
import com.bluetriangle.bluetriangledemo.utils.KEY_SITE_ID
import com.bluetriangle.bluetriangledemo.utils.SCENARIO_APP_CREATE
import com.bluetriangle.bluetriangledemo.utils.TinyDB
import com.bluetriangle.bluetriangledemo.utils.generateDemoWebsiteFromTemplate
import dagger.hilt.android.HiltAndroidApp
import kotlin.system.exitProcess

@HiltAndroidApp
class DemoApplication : Application() {

    var memoryMonitor = MemoryMonitor()

    companion object {
        lateinit var tinyDB: TinyDB
        const val TAG_URL = "TAG_URL"
        const val DEFAULT_TAG_URL = "$DEFAULT_SITE_ID.btttag.com/btt.js"

        private lateinit var instance: DemoApplication

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

        fun restart() {
            val mStartActivity = Intent(instance, LauncherActivity::class.java)
            val pendingIntentId = 123456
            val pendingIntent = PendingIntent.getActivity(
                instance,
                pendingIntentId,
                mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            (instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager)[AlarmManager.RTC, System.currentTimeMillis() + 100] =
                pendingIntent
            exitProcess(0)
        }
    }

    override fun onCreate() {
        super.onCreate()
        tinyDB = TinyDB(applicationContext)

        instance = this

        val launchTest = tinyDB.getBoolean(KEY_LAUNCH_TEST)
        Log.d("DemoApplication", "Application.onCreate: Launch Test: $launchTest")
        val siteId = tinyDB.getString(KEY_SITE_ID, DEFAULT_SITE_ID)

        initTracker(siteId)

        demoWebsiteUrl = "file://${filesDir.absolutePath}/index.html"

        if(!hasTagUrl()) {
            setTagUrl(DEFAULT_TAG_URL)
        }
        checkAndRunLaunchScenario(SCENARIO_APP_CREATE)

        Thread(memoryMonitor).start()
    }

    private fun initTracker(siteId: String?) {
        if (siteId.isNullOrBlank()) return

        val config = ConfigurationManager.getConfig()
        val configuration = BlueTriangleConfiguration()
        configuration.siteId = siteId
        configuration.cacheMemoryLimit = 10 * 1000L
        configuration.cacheExpiryDuration = 120 * 1000L
        configuration.isDebug = true
        Log.d("BlueTriangle", "BTTConfigurationTag received: ${config}")

        if(!config.isDefault) {
            configuration.isTrackAnrEnabled = config.isANRTrackingEnabled
            configuration.isScreenTrackingEnabled = config.isScreenTrackingEnabled
            configuration.isLaunchTimeEnabled = config.isLaunchTimeEnabled
            configuration.isPerformanceMonitorEnabled = config.isPerformanceMonitoringEnabled
            configuration.isTrackCrashesEnabled = config.isCrashTrackingEnabled
            configuration.networkSampleRate = if(config.isNetworkCapturingEnabled) 1.0 else 0.0
            configuration.isMemoryWarningEnabled = config.isMemoryWarningEnabled
            configuration.isTrackNetworkStateEnabled = config.isNetworkStateTrackingEnabled
        }
        Tracker.init(this, configuration)
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