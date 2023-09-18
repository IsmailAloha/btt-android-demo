package com.bluetriangle.bluetriangledemo.tests

import android.util.Log
import com.bluetriangle.android.demo.tests.BTTTestCase
import java.math.BigInteger

class MidCPUUsageTest(private val interval: Long):BTTTestCase {
    override val title: String
        get() = "Medium CPU Usage"
    override val description: String
        get() = "Emulates 50-80% CPU Usage"

    override fun run(): String? {
        val startTime = System.currentTimeMillis()
        var counter = 0u
        while(System.currentTimeMillis() - startTime <= (interval * 1000)) {
            // Doing Nothing
            counter++
            // 8% AVG for 8 cores == 60-70% for 1 core
            Log.d("BlueTriangle", "Counter: $counter")
            if(counter % 200u == 0u) {
                Thread.sleep(1)
            }
//            12% AVG
//            Log.d("BlueTriangle", "CPU Usage : Counter: $counter")

//
//          3% AVG
//          if(counter % 100u == 0u) {
//                Log.d("BlueTriangle", "CPU Usage : Counter: $counter")
//                Thread.sleep(1)
//          }
        }
        return null
    }
}