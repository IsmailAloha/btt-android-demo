package com.bluetriangle.bluetriangledemo.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluetriangle.analytics.Timer
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.InfoPopupDialog
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.TrackedFragment
import com.bluetriangle.bluetriangledemo.adapters.CartItemAdapter
import com.bluetriangle.bluetriangledemo.adapters.ProductAdapter
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.databinding.FragmentCartBinding
import com.bluetriangle.bluetriangledemo.ui.products.ProductsFragmentDirections
import com.bluetriangle.bluetriangledemo.utils.AlertDialogState
import com.bluetriangle.bluetriangledemo.utils.AlertView
import com.bluetriangle.bluetriangledemo.utils.MANUAL_TIMER_SEGMENT
import com.bluetriangle.bluetriangledemo.utils.showAlert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : TrackedFragment(), AlertView {

    private var _binding: FragmentCartBinding? = null

    override fun getPageName() = "CartFragmentManualTimer"

    private val binding get() = _binding!!

    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        cartViewModel.errorHandler.alertView = this

        val cartItemAdapter = CartItemAdapter(requireContext(), {
            cartViewModel.removeCartItem(it)
        }, {
            cartViewModel.reduceQuantity(it)
        }) {
            cartViewModel.increaseQuantity(it)
        }

        binding.productsList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = cartItemAdapter
        }

        cartViewModel.cart.observe(viewLifecycleOwner) { cart ->
            cartItemAdapter.submitList(cart?.items?: emptyList())
        }

        binding.checkoutButton.setOnClickListener {
            cartViewModel.handleCheckoutCrash()
            findNavController().navigate(R.id.action_cart_to_checkout)
        }

        binding.infoButton.isVisible = false
        binding.infoButton.setOnClickListener {
            InfoPopupDialog(it.context, "Cart Scenarios", """
                - Checkout while the cart is empty results in a crash
                - Checkout while there are more than 5 items in cart will result in a crash
            """.trimIndent()).show()
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        cartViewModel.refreshCart()
        Tracker.instance?.setGroupName("The Home Depot México")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showAlert(alertDialogState: AlertDialogState) {
        requireContext().showAlert(alertDialogState)
    }
}