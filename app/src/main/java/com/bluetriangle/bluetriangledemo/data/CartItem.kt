package com.bluetriangle.bluetriangledemo.data

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val id: Long,
    val product: Long,
    val productReference: Product? = null,
    val quantity: Long,
    val price: Double,
) : Parcelable {
    @IgnoredOnParcel
    val total: Double = quantity * price
}
