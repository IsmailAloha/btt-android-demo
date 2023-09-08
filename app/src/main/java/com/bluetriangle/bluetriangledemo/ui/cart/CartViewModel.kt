package com.bluetriangle.bluetriangledemo.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.KEY_LAUNCH_SCENARIO
import com.bluetriangle.bluetriangledemo.KEY_LAUNCH_TEST
import com.bluetriangle.bluetriangledemo.data.Cart
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.data.CartRepository
import com.bluetriangle.bluetriangledemo.utils.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@HiltViewModel
class CartViewModel @Inject constructor(val cartRepository: CartRepository) : ViewModel() {

    private val _cart = MutableLiveData<Cart?>()

    val cart: LiveData<Cart?> = _cart

    val errorHandler = ErrorHandler()
    init {
        refreshCart()
    }

    fun refreshCart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cart = cartRepository.getCart()
                withContext(Dispatchers.Main) {
                    _cart.value = cart
                }
            } catch (e: Exception) {
                errorHandler.showError(e)
            }
        }
    }

    fun handleCheckoutCrash() {
        val cartValue = cart.value
        if(cartValue == null || cartValue.items.isNullOrEmpty()) {
            throw EmptyCartException()
        } else if(cartValue.items.size >= 5) {
            throw CartOverflowException(cartValue.items.size)
        }
    }

    fun removeCartItem(cartItem: CartItem, startTime: Long = System.currentTimeMillis()) {
        val diff = System.currentTimeMillis() - startTime
        if(diff <= 10_000) {
            Thread.sleep(200)
            removeCartItem(cartItem, startTime)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                cartRepository.removeCartItem(cartItem)
                refreshCart()
            }
        }
    }

    fun reduceQuantity(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if(cartItem.quantity <= 1) {
                cartRepository.removeCartItem(cartItem)
            } else {
                cartRepository.reduceQuantity(cartItem)
            }
            refreshCart()
        }
    }

    fun increaseQuantity(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.increaseQuantity(cartItem)
            refreshCart()
        }
    }

    fun handleLaunchScenario() {
        val cartItems = cart.value?.items
        if(cartItems.isNullOrEmpty()) return

        if(cartItems.size == 1) {
            val quantity = cartItems[0].quantity
            if(quantity in 8..11) {
                DemoApplication.tinyDB.setBoolean(KEY_LAUNCH_TEST, true)
                DemoApplication.tinyDB.setInt(KEY_LAUNCH_SCENARIO, (quantity - 7).toInt())
            }
        }
    }
}