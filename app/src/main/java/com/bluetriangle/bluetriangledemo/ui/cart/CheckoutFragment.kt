package com.bluetriangle.bluetriangledemo.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bluetriangle.android.demo.tests.HeavyLoopTest
import com.bluetriangle.bluetriangledemo.databinding.FragmentCheckoutBinding
import com.bluetriangle.bluetriangledemo.utils.AlertDialogState
import com.bluetriangle.bluetriangledemo.utils.AlertView
import com.bluetriangle.bluetriangledemo.utils.showAlert
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckoutFragment : Fragment(), AlertView {

    private var _binding: FragmentCheckoutBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val checkoutViewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]
        checkoutViewModel.errorHandler.alertView = this

        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        checkoutViewModel.cart.observe(viewLifecycleOwner) {
            it?.let { cart ->
                binding.orderNumber.text = String.format("Checkout ID: %s", cart.confirmation)
            }
        }

        binding.continueShopping.setOnClickListener {
            HeavyLoopTest().run()
            findNavController().popBackStack()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showAlert(alertDialogState: AlertDialogState) {
        requireContext().showAlert(alertDialogState)
    }

}