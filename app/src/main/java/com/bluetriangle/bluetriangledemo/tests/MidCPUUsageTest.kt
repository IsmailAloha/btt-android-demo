package com.bluetriangle.bluetriangledemo.tests

class MidCPUUsageTest(private val interval: Long):BTTTestCase {
    override val title: String
        get() = "Medium CPU Usage"
    override val description: String
        get() = "Emulates 50-80% CPU Usage"

    override fun run(): String? {
        val startTime = System.currentTimeMillis()
        var counter = 0
        while(System.currentTimeMillis() - startTime <= (interval * 1000)) {
            // Doing Nothing
            counter++
            // 8% AVG for 8 cores == 60-70% for 1 core
            println(Math.random())
            if(counter % 150 == 0) {
                Thread.sleep(5)
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