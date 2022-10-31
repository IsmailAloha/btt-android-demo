package com.bluetriangle.bluetriangledemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.Tracker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.collections.HashMap

class MainViewModel : ViewModel() {

    val siteId: MutableLiveData<String> = MutableLiveData(SITE_ID)
    val globalUserId: MutableLiveData<String> = MutableLiveData("")
    val sessionId: MutableLiveData<String> = MutableLiveData("")
    val timerConfiguration: MutableLiveData<TimerConfiguration> = MutableLiveData(TimerConfiguration())

    private val requestChannel: MutableSharedFlow<HashMap<String,String>> = MutableSharedFlow(extraBufferCapacity = 1)
    val request: SharedFlow<HashMap<String,String>> = requestChannel.asSharedFlow()

    private var timer: Timer? = null

    init {

        globalUserId.value = Tracker.instance?.getGlobalField(Timer.FIELD_GLOBAL_USER_ID) ?: ""
        sessionId.value = Tracker.instance?.getGlobalField(Timer.FIELD_SESSION_ID) ?: ""
    }

    fun submit() {
        val requestBody = submitTimer() ?: hashMapOf()
        requestChannel.tryEmit(requestBody)
    }

    fun clear() {
        timerConfiguration.value = TimerConfiguration()
    }

    private fun submitTimer(): HashMap<String, String>? {
        val config = timerConfiguration.value ?: return null
        if (timer != null) return null

        try {
            val _timer = Timer().apply {
                configure(config)
            }
            timer = _timer
            _timer.start()

            val allFields = _timer.getFields()
            allFields.forEach { (key, value) ->
                Tracker.instance?.setGlobalField(key, value)
            }
            Tracker.instance?.submitTimer(_timer)
            return HashMap(_timer.getFields())
        } finally {
            timer = null
        }
    }
}