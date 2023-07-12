package com.bluetriangle.bluetriangledemo.ui.products

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.ADD_TO_CART_LIMIT
import com.bluetriangle.bluetriangledemo.data.CartRepository
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.utils.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(val cartRepository: CartRepository) : ViewModel() {

    private val _isAddingToCart = MutableLiveData<Boolean>().apply {
        value = false
    }

    var addToCartClickCount: Int = 0

    val errorHandler = ErrorHandler()

    val isAddingToCart: LiveData<Boolean> = _isAddingToCart

    fun addToCart(v: View, product: Product) {
        addToCartClickCount++

        if(addToCartClickCount > ADD_TO_CART_LIMIT) {
            throw AddToCartLimitExceededException(ADD_TO_CART_LIMIT)
        }
        _isAddingToCart.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cartRepository.addToCart(product)
            } catch (e: Exception) {
                withContext(Main) {
                    errorHandler.showError(e)
                }
            }
            withContext(Main) {
                _isAddingToCart.value = false
            }
        }
    }
}