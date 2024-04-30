package com.bluetriangle.bluetriangledemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfigurationViewModel : ViewModel() {

    private val _isDefault = MutableLiveData(true)
    val isDefault: LiveData<Boolean>
        get() = _isDefault


    private val _isScreenTrackingEnabled = MutableLiveData(true)
    val isScreenTrackingEnabled: LiveData<Boolean>
        get() = _isScreenTrackingEnabled


    private val _isLaunchTimeEnabled = MutableLiveData(true)
    val isLaunchTimeEnabled: LiveData<Boolean>
        get() = _isLaunchTimeEnabled


    private val _isPerformanceMonitoringEnabled = MutableLiveData(true)
    val isPerformanceMonitoringEnabled: LiveData<Boolean>
        get() = _isPerformanceMonitoringEnabled


    private val _isCrashTrackingEnabled = MutableLiveData(true)
    val isCrashTrackingEnabled: LiveData<Boolean>
        get() = _isCrashTrackingEnabled


    private val _isANRTrackingEnabled = MutableLiveData(true)
    val isANRTrackingEnabled: LiveData<Boolean>
        get() = _isANRTrackingEnabled


    private val _isMemoryWarningEnabled = MutableLiveData(true)
    val isMemoryWarningEnabled: LiveData<Boolean>
        get() = _isMemoryWarningEnabled


    private val _isNetworkCapturingEnabled = MutableLiveData(true)
    val isNetworkCapturingEnabled: LiveData<Boolean>
        get() = _isNetworkCapturingEnabled


    private val _isNetworkStateTrackingEnabled = MutableLiveData(true)
    val isNetworkStateTrackingEnabled: LiveData<Boolean>
        get() = _isNetworkStateTrackingEnabled

    fun onKeepDefaultChanged(checked: Boolean) {
        _isDefault.value = checked
    }

    fun onScreenTrackingChanged(checked: Boolean) {
        _isScreenTrackingEnabled.value = checked
    }

    fun onLaunchTimeChanged(checked: Boolean) {
        _isLaunchTimeEnabled.value = checked
    }

    fun onPerformanceMonitoringChanged(checked: Boolean) {
        _isPerformanceMonitoringEnabled.value = checked
    }

    fun onCrashTrackingChanged(checked: Boolean) {
        _isCrashTrackingEnabled.value = checked
    }

    fun onANRTrackingChanged(checked: Boolean) {
        _isANRTrackingEnabled.value = checked
    }

    fun onMemoryWarningChanged(checked: Boolean) {
        _isMemoryWarningEnabled.value = checked
    }

    fun onNetworkCapturingChanged(checked: Boolean) {
        _isNetworkCapturingEnabled.value = checked
    }

    fun onNetworkStateTrackingChanged(checked: Boolean) {
        _isNetworkStateTrackingEnabled.value = checked
    }

    fun onApplyClicked() {
        ConfigurationManager.saveConfig(
            Config(
                _isDefault.value ?: true,
                _isScreenTrackingEnabled.value ?: false,
                _isLaunchTimeEnabled.value ?: false,
                _isPerformanceMonitoringEnabled.value ?: false,
                _isCrashTrackingEnabled.value ?: false,
                _isANRTrackingEnabled.value ?: false,
                _isMemoryWarningEnabled.value ?: false,
                _isNetworkCapturingEnabled.value ?: false,
                _isNetworkStateTrackingEnabled.value ?: false
            )
        )
        Thread.sleep(300)
        restart()
    }

    private fun restart() {
        DemoApplication.restart()
    }

}