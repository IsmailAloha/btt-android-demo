package com.bluetriangle.bluetriangledemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfigurationViewModel : ViewModel() {

    private val config = ConfigurationManager.getConfig()

    private val _isDefault = MutableLiveData(config.isDefault)
    val isDefault: LiveData<Boolean>
        get() = _isDefault


    private val _isScreenTrackingEnabled = MutableLiveData(config.isScreenTrackingEnabled)
    val isScreenTrackingEnabled: LiveData<Boolean>
        get() = _isScreenTrackingEnabled


    private val _isLaunchTimeEnabled = MutableLiveData(config.isLaunchTimeEnabled)
    val isLaunchTimeEnabled: LiveData<Boolean>
        get() = _isLaunchTimeEnabled


    private val _isPerformanceMonitoringEnabled = MutableLiveData(config.isPerformanceMonitoringEnabled)
    val isPerformanceMonitoringEnabled: LiveData<Boolean>
        get() = _isPerformanceMonitoringEnabled


    private val _isCrashTrackingEnabled = MutableLiveData(config.isCrashTrackingEnabled)
    val isCrashTrackingEnabled: LiveData<Boolean>
        get() = _isCrashTrackingEnabled


    private val _isANRTrackingEnabled = MutableLiveData(config.isANRTrackingEnabled)
    val isANRTrackingEnabled: LiveData<Boolean>
        get() = _isANRTrackingEnabled


    private val _isMemoryWarningEnabled = MutableLiveData(config.isMemoryWarningEnabled)
    val isMemoryWarningEnabled: LiveData<Boolean>
        get() = _isMemoryWarningEnabled


    private val _isNetworkCapturingEnabled = MutableLiveData(config.isNetworkCapturingEnabled)
    val isNetworkCapturingEnabled: LiveData<Boolean>
        get() = _isNetworkCapturingEnabled


    private val _isNetworkStateTrackingEnabled = MutableLiveData(config.isNetworkStateTrackingEnabled)
    val isNetworkStateTrackingEnabled: LiveData<Boolean>
        get() = _isNetworkStateTrackingEnabled

    private val _isConfigureOnLaunch = MutableLiveData(ConfigurationManager.shouldConfigureOnLaunch())
    val isConfigureOnLaunch: LiveData<Boolean>
        get() = _isConfigureOnLaunch

    private val _isAddDelay = MutableLiveData(ConfigurationManager.shouldAddDelay())
    val isAddDelay: LiveData<Boolean>
        get() = _isAddDelay

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

    fun onConfigureOnLaunchChanged(checked: Boolean) {
        _isConfigureOnLaunch.value = checked
    }

    fun onAddDelayChanged(checked: Boolean) {
        _isAddDelay.value = checked
    }

    fun onApplyClicked() {
        ConfigurationManager.saveConfig(
            Config(
                _isDefault.value ?: false,
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
        ConfigurationManager.setShouldConfigureOnLaunch(_isConfigureOnLaunch.value?:true)
        ConfigurationManager.setShouldAddDelay(_isAddDelay.value?:false)
        Thread.sleep(300)
        restart()
    }

    private fun restart() {
        DemoApplication.restart()
    }

}