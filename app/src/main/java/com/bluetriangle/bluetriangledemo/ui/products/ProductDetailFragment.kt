package com.bluetriangle.bluetriangledemo.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.bluetriangledemo.DemoApplication
import com.bluetriangle.bluetriangledemo.InfoPopupDialog
import com.bluetriangle.bluetriangledemo.databinding.FragmentProductDetailBinding
import com.bluetriangle.bluetriangledemo.utils.AlertDialogState
import com.bluetriangle.bluetriangledemo.utils.AlertView
import com.bluetriangle.bluetriangledemo.utils.MemoryHolder
import com.bluetriangle.bluetriangledemo.utils.showAlert
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProductDetailFragment : Fragment(), AlertView {

    private var _binding: FragmentProductDetailBinding? = null

    private val binding get() = _binding!!

    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Tracker.instance == null) {
            (requireActivity().application as? DemoApplication)?.initTracker()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val productDetailViewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
        productDetailViewModel.errorHandler.alertView = this
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        binding.apply {
            productName.text = args.product.name
            productPrice.text = String.format(Locale.ENGLISH, "%.2f", args.product.price)
            productDescription.text = args.product.description
            imageSlider.adapter = ImageSliderAdapter(args.product.image, this@ProductDetailFragment)
            addToCartButton.setOnClickListener {
                productDetailViewModel.addToCart(args.product)
            }
            infoButton.isVisible = false
            infoButton.setOnClickListener {
                InfoPopupDialog(
                    it.context,
                    "Add to Cart Scenarios",
                    """
                        |  - Perfume product will generate a heavy memory load
                        |  - KeyHolder will use 50% of CPU capacity""".trimIndent()
                ).show()
            }
        }

        productDetailViewModel.isAddingToCart.observe(viewLifecycleOwner) {
            binding.addToCartButton.isEnabled = !it
        }

        return binding.root
    }

    class ImageSliderAdapter(val image: String, fragment: Fragment):
        FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            return ProductImageFragment(image)
        }

        override fun getItemCount(): Int {
            return 5
        }

    }

    override fun onPause() {
        super.onPause()
        MemoryHolder.clearMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showAlert(alertDialogState: AlertDialogState) {
        requireContext().showAlert(alertDialogState)
    }

}