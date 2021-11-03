package com.bluetriangle.bluetriangledemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.Tracker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MainViewModel : ViewModel() {

    val siteId: MutableLiveData<String> = MutableLiveData(SITE_ID)
    val globalUserId: MutableLiveData<String> = MutableLiveData("")
    val sessionId: MutableLiveData<String> = MutableLiveData("")
    val timerConfiguration: MutableLiveData<TimerConfiguration> = MutableLiveData(TimerConfiguration())

    private val requestChannel: MutableSharedFlow<HashMap<String,String>> = MutableSharedFlow(extraBufferCapacity = 1)
    val request: SharedFlow<HashMap<String,String>> = requestChannel.asSharedFlow()

    private var timer: Timer? = null

    init {
        val fields = Tracker.getInstance().globalFields
        globalUserId.value = fields["gID"] ?: ""
        sessionId.value = fields["sID"] ?: ""
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

            val allFields = _timer.fields
            val requestRepresentation = Tracker.getInstance().globalFields.apply {
                allFields.forEach { (key, value) ->
                    this[key] = value
                }
            }
            Tracker.getInstance().submitTimer(_timer)
            return HashMap(requestRepresentation)
        } finally {
            timer = null
        }
    }
}