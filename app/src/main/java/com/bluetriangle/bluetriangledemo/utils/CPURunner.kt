package com.bluetriangle.bluetriangledemo.utils

import android.system.Os
import android.system.OsConstants
import com.bluetriangle.analytics.Timer
import com.bluetriangle.bluetriangledemo.tests.HeavyLoopTest
import com.bluetriangle.bluetriangledemo.tests.MidCPUUsageTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object CPURunner {

    private var defaultScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    fun fiftyToEightPercentCPU() {
        val timer = Timer("CPUFiftyPercentTimer", "")
        defaultScope.launch(Dispatchers.Default) {
            // Since, whenever a UI event occurs there is a spike in the CPU usage for a fraction of a second
            // so that the spike doesn't mess with our sampling, we are creating this isolated test that starts 1 second
            // after the button click and waits for 1 second then starts the timer then waits for 1 second to start
            // CPU utilization then waits for 1 second and finally submits the timer
            delay(1000)
            timer.start()
            delay(1000)
            MidCPUUsageTest(27L).run()
            delay(1000)
            timer.submit()
        }
    }

    fun fiftyPercentOfDeviceCapacity() {
        val cpuCores = Os.sysconf(OsConstants._SC_NPROCESSORS_CONF)

        repeat(cpuCores.toInt()/2) {
            hundredPercentCPU()
        }
    }

    fun hundredPercentCPU() {
        defaultScope.launch(Dispatchers.Default) {
            HeavyLoopTest(30L).run()
        }
    }
}