package com.bluetriangle.bluetriangledemo.utils

import android.content.Context
import androidx.core.content.edit
import com.bluetriangle.bluetriangledemo.ui.profile.ProfileViewModel.Companion.IS_PREMIUM
import com.bluetriangle.bluetriangledemo.ui.profile.ProfileViewModel.Companion.LOGIN_PREFS
import com.bluetriangle.bluetriangledemo.ui.profile.ProfileViewModel.Companion.USERNAME

class LoginProvider(context: Context) {
    var isPremium: Boolean
        get() = sharedPrefs.getBoolean(IS_PREMIUM, false)
        set(value) = sharedPrefs.edit {
            putBoolean(IS_PREMIUM, value)
        }

    var userName: String?
        get() = sharedPrefs.getString(USERNAME, null)
        set(value) = sharedPrefs.edit {
            putString(USERNAME, value)
        }

    private val sharedPrefs = context.getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE)
}