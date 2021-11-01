package com.bluetriangle.bluetriangledemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluetriangle.analytics.Tracker

class MainViewModel : ViewModel() {

    val siteId: MutableLiveData<String> = MutableLiveData(SITE_ID)
    val globalUserId: MutableLiveData<String> = MutableLiveData("")
    val sessionId: MutableLiveData<String> = MutableLiveData("")
    val timerConfiguration: MutableLiveData<TimerConfiguration> = MutableLiveData(TimerConfiguration())

    init {
        val fields = Tracker.getInstance().globalFields
        globalUserId.value = fields["gID"] ?: ""
        sessionId.value = fields["sID"] ?: ""
    }

    fun submit() {
        //
    }

    fun clear() {
        timerConfiguration.value = TimerConfiguration()
    }
}