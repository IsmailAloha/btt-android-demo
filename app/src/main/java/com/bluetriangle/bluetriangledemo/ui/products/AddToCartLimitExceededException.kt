package com.bluetriangle.bluetriangledemo.ui.products

class AddToCartLimitExceededException(limit:Int) : Exception("Add to cart clicked more than $limit times.")
