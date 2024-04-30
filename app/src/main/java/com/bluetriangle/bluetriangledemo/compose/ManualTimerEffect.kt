package com.bluetriangle.bluetriangledemo.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.utils.MANUAL_TIMER_SEGMENT

@Composable
@NonRestartableComposable
fun ManualTimerEffect(screenName: String) {
    val lifecycleOwner = LocalLifecycleOwner.current
    if(Tracker.instance?.configuration?.isScreenTrackingEnabled == false) {
        DisposableEffect(Unit) {
            val observer = ComposableLifecycleObserver(screenName)
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}

internal class ComposableLifecycleObserver(
    private val screenName: String
) : LifecycleEventObserver {

    private var manualTimer: Timer?=null

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            ON_CREATE, ON_START -> {
                if(manualTimer == null) {
                    manualTimer = Timer(screenName, MANUAL_TIMER_SEGMENT)
                    manualTimer?.start()
                }
            }
            ON_STOP -> {
                manualTimer?.submit()
                manualTimer = null
            }
            else -> {}
        }
    }
}