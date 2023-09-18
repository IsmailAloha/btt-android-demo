package com.bluetriangle.bluetriangledemo.tests

import android.util.Log

class MemoryMonitor:Runnable  {

    private val totalMemory = Runtime.getRuntime().maxMemory()

    data class MemoryWarning(val usedMemory: Long, val totalMemory: Long)

    private var minMemory = Long.MAX_VALUE
    private var maxMemory: Long = 0
    private var cumulativeMemory: Long = 0
    private var memoryCount: Long = 0

    private var isMemoryThresholdReached = false

    private val Long.mb: Long
        get() = this / (1024 * 1024)

    var memoryWarningListener:MemoryWarningListener?=null

    interface MemoryWarningListener {
        fun onMemoryWarning(memoryWarning: MemoryWarning)
    }

    private fun updateMemory(memory: Long) {
        if (memory < minMemory) {
            minMemory = memory
        }
        if (memory > maxMemory) {
            maxMemory = memory
        }
        cumulativeMemory += memory
        memoryCount++
    }

    override fun run() {
        while(true) {
            val usedMemory = Runtime.getRuntime().totalMemory()
            if (usedMemory / totalMemory.toFloat() >= 0.8) {
                if (!isMemoryThresholdReached) {
                    isMemoryThresholdReached = true
                    Log.d("MemoryMonitorLogTag", "onMemoryWarning::${memoryWarningListener}")
                    memoryWarningListener?.onMemoryWarning(MemoryWarning(usedMemory.mb, totalMemory.mb))
                }
            } else {
                isMemoryThresholdReached = false
            }
            updateMemory(usedMemory)
            Thread.sleep(1000)
        }
    }
}