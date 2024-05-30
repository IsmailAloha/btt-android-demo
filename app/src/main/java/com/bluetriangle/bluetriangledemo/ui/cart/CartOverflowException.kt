package com.bluetriangle.bluetriangledemo.ui.cart

class CartOverflowException(cartCount: Int) : Exception("Cart should not have more than 5 products, current product count: $cartCount")