package com.bluetriangle.bluetriangledemo

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bluetriangle.analytics.Tracker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ConfigurationManager {
    private const val SHOULD_ADD_DELAY: String = "SHOULD_ADD_DELAY"
    private const val SHOULD_CONFIGURE_ON_LAUNCH: String = "SHOULD_CONFIGURE_ON_LAUNCH"
    private val gson = Gson()
    private val configType = object: TypeToken<Config>() {}.type

    private const val CONFIG = "CONFIGURATON"

    private val _sessionId = MutableLiveData<String?>()
    val sessionId: LiveData<String?>
        get() = _sessionId

    fun saveConfig(config:Config) {
        val configJson = gson.toJson(config)
        Log.d("BlueTriangle", "BTTConfigurationTag saved: ${configJson}")
        DemoApplication.tinyDB.setString(CONFIG, configJson)
    }

    fun getConfig():Config {
        val configJson = DemoApplication.tinyDB.getString(CONFIG)?:return Config(false)
        Log.d("BlueTriangle", "BTTConfigurationTag received: ${configJson}")
        return gson.fromJson(configJson, configType)
    }

    fun refreshSessionId() {
        Handler(Looper.getMainLooper()).postDelayed({
            _sessionId.postValue(Tracker.instance?.configuration?.sessionId)
        }, 1000)
    }

    fun reset() {
        saveConfig(Config(false))
    }

    fun setShouldConfigureOnLaunch(checked: Boolean) {
        DemoApplication.tinyDB.setBoolean(SHOULD_CONFIGURE_ON_LAUNCH, checked)
    }

    fun shouldConfigureOnLaunch() = DemoApplication.tinyDB.getBoolean(SHOULD_CONFIGURE_ON_LAUNCH, true)

    fun setShouldAddDelay(checked: Boolean) {
        DemoApplication.tinyDB.setBoolean(SHOULD_ADD_DELAY, checked)
    }

    fun shouldAddDelay() = DemoApplication.tinyDB.getBoolean(SHOULD_ADD_DELAY, false)

}