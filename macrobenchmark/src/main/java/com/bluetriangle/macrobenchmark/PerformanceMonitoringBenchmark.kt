package com.bluetriangle.macrobenchmark

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.Tracker
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

@RunWith(AndroidJUnit4::class)
class PerformanceMonitoringBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun testPerformanceOnRunningTimers() {
        val mockApplication = mock<Application>()
        val mockPackageManager = mock<PackageManager>()
        val mockPackageInfo = mock<PackageInfo>()
        val mockResources = mock<Resources>()
        `when`(mockPackageManager.getPackageInfo(any<String>(), any<Int>())).thenReturn(mockPackageInfo)
        `when`(mockApplication.packageManager).thenReturn(mockPackageManager)
        `when`(mockApplication.resources).thenReturn(mockResources)
        Tracker.init(mockApplication)

        val metricsToMeasure = listOf(
            TraceSectionMetric(
                sectionName = "measureBlock",
                mode = TraceSectionMetric.Mode.Sum,
                label = "CpuMeasureBlock",
                targetPackageOnly = false
            ),

            MemoryUsageMetric(
                mode = MemoryUsageMetric.Mode.Max
            )
        )

        benchmarkRule.measureRepeated(
            packageName = "com.bluetriangle.bluetriangledemo.compose",
            metrics = metricsToMeasure,
            iterations = 10,
            setupBlock = {
                // Setup: e.g., restarting the activity or resetting state
            },
            measureBlock = {
                // The actual work you want to measure
                runTimers(20)
            }
        )
    }

    fun runTimers(timersCount: Int) {
        val timers = arrayListOf<Timer>()

        val startTimersThread = Thread {
            repeat(timersCount) {
                val timer = Timer()
                timer.setPageName("Timer $it")
                timer.setTrafficSegmentName("Performance Monitoring Benchmarking")
                timer.start()
                timers.add(timer)
            }
            Thread.sleep(5000)
        }
        startTimersThread.start()
        startTimersThread.join()

        repeat(timers.size) {
            timers[it].end()
        }
    }
}