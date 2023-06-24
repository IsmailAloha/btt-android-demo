package com.bluetriangle.bluetriangledemo.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.api.StoreService
import com.bluetriangle.bluetriangledemo.data.Cart
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.data.CartRepository
import com.bluetriangle.bluetriangledemo.data.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val cartRepository: CartRepository) : ViewModel() {

    private val _cart = MutableLiveData<Cart?>()

    val cart: LiveData<Cart?> = _cart

    init {
        refreshCart()
    }

    fun refreshCart() {
        viewModelScope.launch(Dispatchers.IO) {
            val cart = cartRepository.getCart()
            withContext(Dispatchers.Main) {
                _cart.value = cart
            }
        }
    }

    fun removeCartItem(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val cart = cartRepository.removeCartItem(cartItem)
            withContext(Dispatchers.Main) {
                _cart.value = cart
            }
        }
    }
}