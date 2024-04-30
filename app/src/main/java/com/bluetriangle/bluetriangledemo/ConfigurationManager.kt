package com.bluetriangle.bluetriangledemo

import android.util.Log
import com.bluetriangle.bluetriangledemo.utils.CONFIG
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.JsonReader

object ConfigurationManager {
    private val gson = Gson()
    private val configType = object: TypeToken<Config>() {}.type

    fun saveConfig(config:Config) {
        val configJson = gson.toJson(config)
        Log.d("BlueTriangle", "BTTConfigurationTag saved: ${configJson}")
        DemoApplication.tinyDB.setString(CONFIG, configJson)
    }

    fun getConfig():Config {
        val configJson = DemoApplication.tinyDB.getString(CONFIG)?:return Config(true)
        Log.d("BlueTriangle", "BTTConfigurationTag received: ${configJson}")
        return gson.fromJson(configJson, configType)
    }

    fun reset() {
        saveConfig(Config(true))
    }

}