package com.bluetriangle.bluetriangledemo.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.data.ProductRepository
import com.bluetriangle.bluetriangledemo.utils.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productsRepository: ProductRepository) :
    ViewModel() {

    private val _products = MutableLiveData<List<Product>>().apply {
        value = emptyList()
    }

    val errorHandler = ErrorHandler()
    val products: LiveData<List<Product>> = _products

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = productsRepository.listProducts(skipCache = true)
                withContext(Dispatchers.Main) {
                    _products.value = products.map {
                        when (it.id) {
                            25L -> it.copy(image = "https://publicobject.com/helloworld.txt")
                            26L -> it.copy(image = "https://dnsrequestwouldfailhere.com/")
                            27L -> it.copy(image = "https://untrusted-root.badssl.com/")
                            28L -> it.copy(image = "https://192.169.2.111/")
                            29L -> it.copy(image = "https://httpbin.org/invalidendpoint")
                            30L -> it.copy(image = "https://hub.dummyapis.com/delay?seconds=60")
                            else -> it
                        }
                    }
                }
            } catch (e: Exception) {
                errorHandler.showError(e)
            }
        }
    }
}