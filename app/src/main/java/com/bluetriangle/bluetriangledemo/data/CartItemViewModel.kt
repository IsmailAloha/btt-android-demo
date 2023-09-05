package com.bluetriangle.bluetriangledemo.data

import android.util.Log
import kotlinx.parcelize.IgnoredOnParcel

class CartItemViewModel(val cartItem:CartItem) {

    @IgnoredOnParcel
    var data:ByteArray? = null

    init {
        if(cartItem.productReference?.name?.contains("Perfume") == true) {
            data = null
            Runtime.getRuntime().gc()
            Thread.sleep(300)
            Log.d("BlueTriangle", "Memory Warning: Allocating memory: ${cartItem.quantity}")
            data = ByteArray(cartItem.quantity.toInt() * 100 * 1024 * 1024)
        }
    }
}