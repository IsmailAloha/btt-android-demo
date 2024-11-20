package com.bluetriangle.bluetriangledemo.ui.settings

import android.os.Build
import androidx.lifecycle.ViewModel
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.BuildConfig
import com.bluetriangle.bluetriangledemo.ConfigurationManager
import java.lang.reflect.Field

class SettingsViewModel : ViewModel() {

    val androidVersionName = getAndroidVersion()
    val appVersion = String.format("%s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
    val sdkVersion = com.bluetriangle.analytics.BuildConfig.SDK_VERSION
    val flavor = BuildConfig.FLAVOR.uppercase()
    val siteId = Tracker.instance!!.configuration.siteId
    val anrEnabled = if(Tracker.instance!!.configuration.isTrackAnrEnabled) "Enabled" else "Disabled"
    val screenTrackingEnabled = if(Tracker.instance!!.configuration.isScreenTrackingEnabled) "Enabled" else "Disabled"

    fun getAndroidVersion():String {
        val builder = StringBuilder()
        builder.append(Build.VERSION.RELEASE)

        val fields: Array<Field> = Build.VERSION_CODES::class.java.fields
        for (field in fields) {
            val fieldName: String = field.name
            var fieldValue = -1
            try {
                fieldValue = field.getInt(Any())
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" (").append(fieldName).append(") ")
                builder.append("SDK ").append(fieldValue)
            }
        }
        return builder.toString()
    }

    fun testManualTimer() {
        Timer("Manual Timer", "DemoApplicationTraffic").start().end().submit()
    }

    fun sendBrandValueTimer() {
        val timer = Timer()
        timer.start()
        timer.setPageName("SignUp")
        timer.setBrandValue(99.99)
        timer.submit()
    }
}