package com.bluetriangle.bluetriangledemo.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun Context.dp(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun ImageView.loadImage(url:String?) {
    Glide.with(context)
        .load(url)
        .transform(
            MultiTransformation(
                CenterCrop(),
                RoundedCorners(context.dp(8))
            )
        )
        .into(this)
}