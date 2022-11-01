package com.bluetriangle.bluetriangledemo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val id: Long,
    val confirmation: String,
    val shipping: String,
    val items: List<CartItem>,
) : Parcelable
