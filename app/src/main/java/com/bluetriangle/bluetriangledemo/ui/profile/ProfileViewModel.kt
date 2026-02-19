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
import com.bluetriangle.bluetriangledemo.utils.LoginProvider
import com.bluetriangle.bluetriangledemo.utils.UserType
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val LOGIN_PREFS = "LOGIN_PREFS"
        const val USERNAME = "USERNAME"
        const val IS_PREMIUM = "IS_PREMIUM"
    }

    val loginProvider = LoginProvider(application)

    var loggedInUser = MutableStateFlow(loginProvider.userName)

    var isPremium = MutableStateFlow(loginProvider.isPremium)

    fun login(username: String, isPremium: Boolean) {
        Tracker.instance?.apply {
            setCustomVariable(BTTCustomVariables.User.key, username)
            setCustomVariable(BTTCustomVariables.Premium.key, isPremium)
        }

        loginProvider.userName = username
        loginProvider.isPremium = isPremium

        Tracker.instance?.setCustomCategory1(if(isPremium) UserType.Premium.type else UserType.Standard.type)

        this@ProfileViewModel.isPremium.value = isPremium
        this@ProfileViewModel.loggedInUser.value = username
    }

    fun logout() {
        Tracker.instance?.apply {
            clearCustomVariable(BTTCustomVariables.User.key)
            clearCustomVariable(BTTCustomVariables.Premium.key)
        }

        loginProvider.userName = null
        loginProvider.isPremium = false

        Tracker.instance?.setCustomCategory1(UserType.Guest.type)

        this@ProfileViewModel.loggedInUser.value = null
    }

}