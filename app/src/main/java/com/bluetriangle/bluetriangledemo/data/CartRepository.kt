package com.bluetriangle.bluetriangledemo.data

import android.content.Context
import android.util.Log
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

    private val memoryBank = arrayListOf<MemoryBlock>()

    class MemoryBlock {
        private val memory = ByteArray((Runtime.getRuntime().totalMemory() * 0.29).toInt())
        init {
            Log.d("MemoryBlock","Allocated Memory Block of Size: ${memory.size} * 4 bytes")
        }
    }

    suspend fun getCart(): Cart? {
        val cartId = sharedPreferences.getLong(KEY_CART_ID, 0)
        if (cartId > 0) {
            val products = productRepository.listProducts()
            val productMap = products.associateBy { it.id }
            val cart = storeService.getCart(cartId)
            val productCartItems = cart.items?.map { it.copy(productReference = productMap[it.product]) } ?: emptyList()
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
        val existing = cart.items?.find { it.product == product.id }
        if (existing != null) {
            storeService.updateQuantity(existing.id, existing.quantity + 1)
        } else {
            storeService.addToCart(product.id, 1, product.price, cart.id)
        }
    }

    suspend fun checkout(cart: Cart) {
        storeService.checkout(cart.id)
        sharedPreferences.edit(commit = true) {
            remove(KEY_CART_ID)
        }
    }

    suspend fun removeCartItem(cartItem: CartItem): Cart? {
        storeService.deleteCartItem(cartItem.id)
        return getCart()
    }

    fun allocateMemory() {
        memoryBank.add(MemoryBlock())
    }

    fun deallocateMemory() {
        if(memoryBank.isNotEmpty()) {
            memoryBank.removeFirst()
        }
    }

    fun clearMemory() {
        memoryBank.clear()
    }

    suspend fun reduceQuantity(cartItem:CartItem) {
        storeService.updateQuantity(cartItem.id, cartItem.quantity - 1)
    }

    suspend fun increaseQuantity(cartItem:CartItem) {
        storeService.updateQuantity(cartItem.id, cartItem.quantity + 1)
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "BTDA_CART"
        const val KEY_CART_ID = "cart_id"
    }
}