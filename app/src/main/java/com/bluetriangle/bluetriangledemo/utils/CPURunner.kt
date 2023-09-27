package com.bluetriangle.bluetriangledemo.utils

import com.bluetriangle.android.demo.tests.HeavyLoopTest
import com.bluetriangle.bluetriangledemo.tests.MidCPUUsageTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CPURunner {

    private var defaultScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    fun fiftyToEightPercentCPU() {
        defaultScope.launch(Dispatchers.Default) {
            MidCPUUsageTest(20L).run()
        }
    }

    fun twoHundredPercentCPU() {
        hundredPercentCPU()
        hundredPercentCPU()
    }

    fun hundredPercentCPU() {
        defaultScope.launch(Dispatchers.Default) {
            HeavyLoopTest(20L).run()
        }
    }
}