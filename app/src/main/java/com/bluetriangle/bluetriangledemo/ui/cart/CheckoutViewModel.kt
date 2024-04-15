package com.bluetriangle.bluetriangledemo.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.data.Cart
import com.bluetriangle.bluetriangledemo.data.CartRepository
import com.bluetriangle.bluetriangledemo.utils.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {
    private val _cart = MutableLiveData<Cart?>()

    val cart: LiveData<Cart?> = _cart
    val errorHandler = ErrorHandler()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cart = cartRepository.getCart()
                cart?.let {
                    cartRepository.checkout(it)
                }

                withContext(Dispatchers.Main) {
                    _cart.value = cart?.copy(confirmation = UUID.randomUUID().toString(), shipping = "9.99")
                }
            } catch (e: Exception) {
                errorHandler.showError(e)
            }
        }
    }

}