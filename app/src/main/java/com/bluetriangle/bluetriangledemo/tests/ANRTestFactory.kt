package com.bluetriangle.android.demo.tests

import com.bluetriangle.bluetriangledemo.tests.BTTTestCase
import com.bluetriangle.bluetriangledemo.tests.HeavyLoopTest

object ANRTestFactory {

    fun getANRTests(): List<BTTTestCase> {
        return listOf(
            SleepMainThreadTest(),
            HeavyLoopTest(),
            DownloadTest(),
            DeadLockMainThreadTest()
        )
    }

    fun getANRTest(anrTest: ANRTest): BTTTestCase {
        return when (anrTest) {
            ANRTest.SleepMainThreadTest -> SleepMainThreadTest()
            ANRTest.HeavyLoopTest -> HeavyLoopTest()
            ANRTest.DownloadTest -> DownloadTest()
            else -> DeadLockMainThreadTest()
        }
    }
}


