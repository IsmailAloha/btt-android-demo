package com.bluetriangle.bluetriangledemo.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.data.Cart
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.data.CartRepository
import com.bluetriangle.bluetriangledemo.utils.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

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

    fun removeCartItem(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cart = cartRepository.removeCartItem(cartItem)
                withContext(Dispatchers.Main) {
                    _cart.value = cart
                }
            } catch (e: Exception) {
                errorHandler.showError(e)
            }
        }
    }
}