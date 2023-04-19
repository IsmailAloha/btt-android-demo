package com.bluetriangle.bluetriangledemo.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.data.CartRepository
import com.bluetriangle.bluetriangledemo.data.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    private val _isAddingToCart = MutableLiveData<Boolean>().apply {
        value = false
    }

    val isAddingToCart: LiveData<Boolean> = _isAddingToCart

    fun addToCart(product: Product) {
        _isAddingToCart.value = true
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.addToCart(product)
            withContext(Dispatchers.Main) {
                _isAddingToCart.value = false
            }
        }
    }
}