package com.bluetriangle.bluetriangledemo.ui.products

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.data.CartRepository
import com.bluetriangle.bluetriangledemo.data.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(val cartRepository: CartRepository) : ViewModel() {

    private val _isAddingToCart = MutableLiveData<Boolean>().apply {
        value = false
    }

    val isAddingToCart: LiveData<Boolean> = _isAddingToCart

    fun addToCart(v: View, product: Product) {
        _isAddingToCart.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cartRepository.addToCart(product)
            } catch (e: Exception) {
                withContext(Main) {
                    AlertDialog.Builder(v.context)
                        .setTitle("Exception")
                        .setMessage(e.message)
                        .setPositiveButton("Ok") { dialog,_ ->
                            dialog.dismiss()
                        }.show()
                }
            }
            withContext(Dispatchers.Main) {
                _isAddingToCart.value = false
            }
        }
    }
}