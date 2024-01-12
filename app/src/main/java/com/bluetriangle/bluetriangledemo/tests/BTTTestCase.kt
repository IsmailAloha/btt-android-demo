package com.bluetriangle.bluetriangledemo.tests

interface BTTTestCase {

    val title: String

    val description: String

    fun run(): String?

}