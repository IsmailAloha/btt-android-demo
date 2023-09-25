package com.bluetriangle.bluetriangledemo.utils

import android.util.Log
import java.util.LinkedList

object MemoryHolder {

    private var memoryBank:LinkedList<MemoryBlock>? = null

    class MemoryBlock {
        private val memory = ByteArray((Runtime.getRuntime().maxMemory() * 0.20).toInt())
        init {
            Log.d("MemoryBlock","Allocated Memory Block of Size: ${memory.size} * 4 bytes")
        }
    }

    fun allocateMemory() {
        if(memoryBank == null) {
            memoryBank = LinkedList()
        }
        memoryBank?.add(MemoryBlock())
        Log.d("CartRepoMemoryAllocTag", "${this} : allocateMemory : ${memoryBank?.size}")
    }

    fun deallocateMemory() {
        if(memoryBank == null) return
        if(!memoryBank.isNullOrEmpty()) {
            memoryBank?.removeFirst()
        }
        Log.d("CartRepoMemoryAllocTag", "${this} : deallocateMemory : ${memoryBank?.size}")
    }

    fun clearMemory():Boolean {
        if(memoryBank.isNullOrEmpty()) return false
        memoryBank?.clear()
        memoryBank = null
        Runtime.getRuntime().gc()
        Log.d("CartRepoMemoryAllocTag", "$this : clearedMemory")
        return true
    }
}