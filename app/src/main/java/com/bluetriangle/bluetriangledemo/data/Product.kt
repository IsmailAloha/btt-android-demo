package com.bluetriangle.bluetriangledemo.data

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
) : Parcelable {
    @IgnoredOnParcel
    val isKeyHolder: Boolean
        get() = name == "Key Holder"

    @IgnoredOnParcel
    val isOppo: Boolean
        get() = name == "OPPOF19"

    @IgnoredOnParcel
    val isPerfume: Boolean
        get() = name.contains("Perfume", true)
}
