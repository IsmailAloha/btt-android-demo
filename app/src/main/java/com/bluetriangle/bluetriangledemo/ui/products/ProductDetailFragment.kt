package com.bluetriangle.bluetriangledemo.ui.products

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bluetriangle.bluetriangledemo.databinding.FragmentProductDetailBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null

    private val binding get() = _binding!!

    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val productDetailViewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        binding.apply {
            productName.text = args.product.name
            productPrice.text = "$${args.product.price}"
            productDescription.text = args.product.description
            Glide.with(requireContext()).load(args.product.image).into(productImage)
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

}