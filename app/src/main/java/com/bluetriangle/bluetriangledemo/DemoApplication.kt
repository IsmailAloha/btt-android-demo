package com.bluetriangle.bluetriangledemo

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.util.Log
import com.bluetriangle.analytics.BlueTriangleConfiguration
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.tests.HeavyLoopTest
import com.bluetriangle.bluetriangledemo.tests.MemoryMonitor
import com.bluetriangle.bluetriangledemo.utils.DEFAULT_SITE_ID
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

        fun checkAndAddDelay() {
            if(ConfigurationManager.shouldAddDelay()) {
                HeavyLoopTest(1L).run()
            }
        }

        fun restart() {
            (instance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).appTasks.forEach { it.finishAndRemoveTask() }
            exitProcess(0)
        }

    }

    override fun onCreate() {
        super.onCreate()
        tinyDB = TinyDB(applicationContext)

        instance = this

        if(ConfigurationManager.shouldConfigureOnLaunch()) {
            initTracker()
        }

        demoWebsiteUrl = "file://${filesDir.absolutePath}/index.html"

        if(!hasTagUrl()) {
            setTagUrl(DEFAULT_TAG_URL)
        }
        checkAndAddDelay()

        Thread(memoryMonitor).start()
    }

    fun initTracker() {
        val config = ConfigurationManager.getConfig()
        val configuration = BlueTriangleConfiguration()
        configuration.siteId = DEFAULT_SITE_ID
        configuration.cacheMemoryLimit = 10 * 1000L
        configuration.cacheExpiryDuration = 120 * 1000L
        configuration.isDebug = true
        configuration.debugLevel = Log.VERBOSE
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