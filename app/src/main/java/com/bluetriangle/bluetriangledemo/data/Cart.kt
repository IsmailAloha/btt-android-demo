package com.bluetriangle.bluetriangledemo.data

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val id: Long,
    val confirmation: String,
    val shipping: String,
    val items: List<CartItem>?,
) : Parcelable {
    @IgnoredOnParcel
    val total: Double = items?.sumOf { it.total } ?: 0.0
}
