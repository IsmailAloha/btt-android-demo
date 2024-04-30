package com.bluetriangle.bluetriangledemo

data class Config(
    val isDefault: Boolean,
    val isScreenTrackingEnabled:Boolean = true,
    val isLaunchTimeEnabled:Boolean = true,
    val isPerformanceMonitoringEnabled:Boolean = true,
    val isCrashTrackingEnabled:Boolean = true,
    val isANRTrackingEnabled:Boolean = true,
    val isMemoryWarningEnabled:Boolean = true,
    val isNetworkCapturingEnabled:Boolean = true,
    val isNetworkStateTrackingEnabled:Boolean = true
)
