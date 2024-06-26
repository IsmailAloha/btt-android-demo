package com.bluetriangle.bluetriangledemo.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluetriangle.bluetriangledemo.TrackedFragment
import com.bluetriangle.bluetriangledemo.adapters.ProductAdapter
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.databinding.FragmentProductsBinding
import com.bluetriangle.bluetriangledemo.utils.AlertDialogState
import com.bluetriangle.bluetriangledemo.utils.AlertView
import com.bluetriangle.bluetriangledemo.utils.showAlert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : TrackedFragment(), AlertView {

    private var _binding: FragmentProductsBinding? = null

    private val binding get() = _binding!!
    override fun getPageName() = "ProductsFragmentManualTimer"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        productsViewModel.errorHandler.alertView = this
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        val productAdapter = ProductAdapter(requireContext()) { product: Product ->
            val action = ProductsFragmentDirections.actionProductListToProductDetail(product)
            findNavController().navigate(action)
        }

        binding.productsList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }

        productsViewModel.products.observe(viewLifecycleOwner) {
            productAdapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showAlert(alertDialogState: AlertDialogState) {
        requireContext().showAlert(alertDialogState)
    }
}