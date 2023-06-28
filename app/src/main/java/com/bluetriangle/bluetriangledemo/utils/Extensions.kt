package com.bluetriangle.bluetriangledemo.utils

import android.content.Context

fun Context.dp(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}