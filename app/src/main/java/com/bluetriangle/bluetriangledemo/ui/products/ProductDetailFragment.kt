package com.bluetriangle.bluetriangledemo.ui.products

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bluetriangle.bluetriangledemo.databinding.FragmentProductDetailBinding
import com.bluetriangle.bluetriangledemo.utils.AlertDialogState
import com.bluetriangle.bluetriangledemo.utils.AlertView
import com.bluetriangle.bluetriangledemo.utils.dp
import com.bluetriangle.bluetriangledemo.utils.loadImage
import com.bluetriangle.bluetriangledemo.utils.showAlert
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment(), AlertView {

    private var _binding: FragmentProductDetailBinding? = null

    private val binding get() = _binding!!

    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val productDetailViewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
        productDetailViewModel.errorHandler.alertView = this
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        binding.apply {
            productName.text = args.product.name
            productPrice.text = String.format("%.2f", args.product.price)
            productDescription.text = args.product.description
            productImage.loadImage(args.product.image)
            addToCartButton.setOnClickListener { v ->
                productDetailViewModel.addToCart(v, args.product)
            }
        }

        productDetailViewModel.isAddingToCart.observe(viewLifecycleOwner) {
            binding.addToCartButton.isEnabled = !it
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