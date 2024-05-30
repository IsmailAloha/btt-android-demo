package com.bluetriangle.bluetriangledemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.utils.MANUAL_TIMER_SEGMENT

abstract class TrackedFragment: Fragment() {

    abstract fun getPageName():String

    private var manualTimer: Timer?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if(Tracker.instance?.configuration?.isScreenTrackingEnabled == false) {
            manualTimer = Timer(getPageName(), MANUAL_TIMER_SEGMENT)
            manualTimer?.start()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        if(manualTimer == null && Tracker.instance?.configuration?.isScreenTrackingEnabled == false) {
            manualTimer = Timer(getPageName(), MANUAL_TIMER_SEGMENT)
            manualTimer?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        manualTimer?.submit()
        manualTimer = null
    }
}