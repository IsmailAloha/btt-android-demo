package com.bluetriangle.bluetriangledemo.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.adapters.CartItemAdapter
import com.bluetriangle.bluetriangledemo.adapters.ProductAdapter
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.databinding.FragmentCartBinding
import com.bluetriangle.bluetriangledemo.ui.products.ProductsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    private val binding get() = _binding!!

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cartItemAdapter = CartItemAdapter(requireContext()) {
            cartViewModel.removeCartItem(it)
        }

        binding.productsList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = cartItemAdapter
        }

        cartViewModel.cart.observe(viewLifecycleOwner) { cart ->
            if (cart == null || cart.items.isNullOrEmpty()) {
                binding.checkoutButton.apply {
                    setText(R.string.empty_cart)
                    isEnabled = false
                }
                cartItemAdapter.submitList(emptyList())
            } else {
                binding.checkoutButton.apply {
                    setText(R.string.checkout)
                    isEnabled = true
                }
                cartItemAdapter.submitList(cart.items)
            }
        }

        binding.checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_cart_to_checkout)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.refreshCart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}