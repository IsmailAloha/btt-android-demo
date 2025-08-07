package com.bluetriangle.bluetriangledemo

import android.content.Context
import android.util.Log
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.SessionStore.SessionData.Companion.toJsonObject
import com.bluetriangle.bluetriangledemo.SessionStore.SessionData.Companion.toSessionData
import org.json.JSONObject

class SessionStore(val context: Context) {

    companion object {
        private const val SESSION_STORE = "session_store"
        private const val SESSION_DATA = "session_data"
    }

    private val storePrefs = context.getSharedPreferences(SESSION_STORE, Context.MODE_PRIVATE)

    fun resetSessionExpiration() {
        val sessionData = retrieveSessionData()
        val updatedExpiry = System.currentTimeMillis() + 2 * 60 * 1000
        Log.d("SessionStoreLog", "Resetting Expiry for $sessionData to $updatedExpiry")
        if (sessionData != null) {
            storeSessionData(
                SessionData(
                    sessionData.sessionId,
                    sessionData.shouldSampleNetwork,
                    sessionData.isConfigApplied,
                    sessionData.networkSampleRate,
                    updatedExpiry
                )
            )
        }

    }

    internal data class SessionData(
        val sessionId: String,
        val shouldSampleNetwork: Boolean,
        val isConfigApplied: Boolean,
        val networkSampleRate: Double,
        val expiration: Long
    ) {
        companion object {
            private const val SESSION_ID = "sessionId"
            private const val EXPIRATION = "expiration"
            private const val SHOULD_SAMPLE_NETWORK = "shouldSampleNetwork"
            private const val IS_CONFIG_APPLIED = "isConfigApplied"
            private const val NETWORK_SAMPLE_RATE = "networkSampleRate"

            internal fun JSONObject.toSessionData(): SessionData? {
                try {
                    return SessionData(
                        sessionId = getString(SESSION_ID),
                        shouldSampleNetwork = getBoolean(SHOULD_SAMPLE_NETWORK),
                        isConfigApplied = getBoolean(IS_CONFIG_APPLIED),
                        networkSampleRate = getDouble(NETWORK_SAMPLE_RATE),
                        expiration = getLong(EXPIRATION)
                    )
                } catch (e: Exception) {
                    Tracker.instance?.configuration?.logger?.error("Error while parsing session data: ${e::class.simpleName}(\"${e.message}\")")
                    return null
                }
            }

            internal fun SessionData.toJsonObject() = JSONObject().apply {
                put(SESSION_ID, sessionId)
                put(SHOULD_SAMPLE_NETWORK, shouldSampleNetwork)
                put(IS_CONFIG_APPLIED, isConfigApplied)
                put(NETWORK_SAMPLE_RATE, networkSampleRate)
                put(EXPIRATION, expiration)
            }
        }
    }

    private fun storeSessionData(sessionData: SessionData) {
        storePrefs.edit()
            .putString("${SESSION_DATA}_${Tracker.instance?.configuration?.siteId}", sessionData.toJsonObject().toString())
            .apply()
    }

    private fun retrieveSessionData(): SessionData? {
        val sessionDataJSON =
            storePrefs.getString("${SESSION_DATA}_${Tracker.instance?.configuration?.siteId}", null)
                ?: return null
        return JSONObject(sessionDataJSON).toSessionData()
    }

}