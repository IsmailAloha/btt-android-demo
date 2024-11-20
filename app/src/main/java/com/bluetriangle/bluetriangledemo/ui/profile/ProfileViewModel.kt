package com.bluetriangle.bluetriangledemo.ui.profile

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.utils.BTTCustomVariables
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val LOGIN_PREFS = "LOGIN_PREFS"
        const val USERNAME = "USERNAME"
        const val IS_PREMIUM = "IS_PREMIUM"
    }

    private val sharedPreferences = application.getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE)

    var loggedInUser = MutableStateFlow(sharedPreferences.getString(USERNAME, null))

    var isPremium = MutableStateFlow(sharedPreferences.getBoolean(IS_PREMIUM, false))

    fun login(username: String, isPremium: Boolean) {
        Tracker.instance?.apply {
            setCustomVariable(BTTCustomVariables.User.key, username)
            setCustomVariable(BTTCustomVariables.Premium.key, isPremium)
        }

        sharedPreferences.edit {
            putString(USERNAME, username)
            putBoolean(IS_PREMIUM, isPremium)
            this@ProfileViewModel.isPremium.value = isPremium
            this@ProfileViewModel.loggedInUser.value = username
        }
    }

    fun logout() {
        Tracker.instance?.apply {
            clearCustomVariable(BTTCustomVariables.User.key)
            clearCustomVariable(BTTCustomVariables.Premium.key)
        }

        sharedPreferences.edit {
            remove(USERNAME)
            remove(IS_PREMIUM)
            this@ProfileViewModel.loggedInUser.value = null
        }
    }

}