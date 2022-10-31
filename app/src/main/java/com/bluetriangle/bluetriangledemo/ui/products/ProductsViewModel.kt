package com.bluetriangle.bluetriangledemo.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.api.StoreService
import com.bluetriangle.bluetriangledemo.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>().apply {
        value = emptyList()
    }

    val products: LiveData<List<Product>> = _products

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val storeService = StoreService.create()
            val products = storeService.listProducts()
            withContext(Dispatchers.Main) {
                _products.value = products
            }
        }
    }
}