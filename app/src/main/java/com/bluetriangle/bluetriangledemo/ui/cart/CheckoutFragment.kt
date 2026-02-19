package com.bluetriangle.bluetriangledemo.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.tests.HeavyLoopTest
import com.bluetriangle.bluetriangledemo.databinding.FragmentCheckoutBinding
import com.bluetriangle.bluetriangledemo.utils.AlertDialogState
import com.bluetriangle.bluetriangledemo.utils.AlertView
import com.bluetriangle.bluetriangledemo.utils.showAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

        setUpCheckoutSteps()

        return root
    }

    private fun setUpCheckoutSteps() {
        lifecycleScope.launch(Default) {
            var last: CheckoutStepFragment = PaymentConfirmationFragment()
            listOf(
                OrderConfirmationFragment(),
                OrderSuccessfulFragment()
            ).forEach {
                delay(1000)
                withContext(Main) {
                    last = it
                    childFragmentManager.beginTransaction().replace(R.id.checkoutStepsContainer, it).addToBackStack(it.javaClass.simpleName).commit()
                }
            }
//            delay(1000)
//            withContext(Main) {
//                childFragmentManager.beginTransaction().remove(last).commit()
//                binding.checkoutStepsContainer.isVisible = false
//                binding.checkoutGroup.isVisible = true
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showAlert(alertDialogState: AlertDialogState) {
        requireContext().showAlert(alertDialogState)
    }

}