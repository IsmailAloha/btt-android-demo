package com.bluetriangle.bluetriangledemo.data

import android.content.Context
import androidx.core.content.edit
import com.bluetriangle.bluetriangledemo.api.StoreService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productRepository: ProductRepository,
    private val storeService: StoreService
) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    suspend fun getCart(): Cart? {
        val cartId = sharedPreferences.getLong(KEY_CART_ID, 0)
        if (cartId > 0) {
            val products = productRepository.listProducts()
            val productMap = products.associateBy { it.id }
            val cart = storeService.getCart(cartId)
            val productCartItems = cart.items.map { it.copy(productReference = productMap[it.product]) }
            return cart.copy(items = productCartItems)
        }
        return null
    }

    suspend fun addToCart(product: Product) {
        var cart = getCart()
        if (cart == null) {
            cart = storeService.createCart(null)
            sharedPreferences.edit(commit = true) {
                putLong(KEY_CART_ID, cart.id)
            }
        }
        val existing = cart.items.find { it.product == product.id }
        if (existing != null) {
            storeService.updateQuantity(existing.id, existing.quantity + 1)
        } else {
            storeService.addToCart(product.id, 1, product.price, cart.id)
        }
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "BTDA_CART"
        const val KEY_CART_ID = "cart_id"
    }
}