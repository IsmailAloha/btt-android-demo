package com.bluetriangle.bluetriangledemo

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SessionStore(val context: Context) {

    companion object {
        private const val SESSION_STORE = "session_store"
        private const val SESSION_DATA = "session_data"
    }

    private val storePrefs = context.getSharedPreferences(SESSION_STORE, Context.MODE_PRIVATE)

    fun resetSessionExpiration() {
        val sessionData = retrieveSessionData()
        Log.d("SessionStoreLog", "Resetting : $sessionData")
        if(sessionData != null) {
            storeSessionData(
                SessionData(
                sessionData.sessionId,
                System.currentTimeMillis() + 2 * 60 * 1000
            ))
        }

    }

    data class SessionData(
        val sessionId: String,
        val expiration:Long
    )

    private fun storeSessionData(sessionData: SessionData) {
        storePrefs.edit()
            .putString(SESSION_DATA, Gson().toJson(sessionData))
            .apply()
    }

    private fun retrieveSessionData(): SessionData? {
        val sessionDataJSON = storePrefs.getString(SESSION_DATA, null)?:return null
        return Gson().fromJson(sessionDataJSON, object: TypeToken<SessionData>() {}.type)
    }

}