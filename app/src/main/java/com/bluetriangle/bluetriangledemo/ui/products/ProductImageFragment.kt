package com.bluetriangle.bluetriangledemo.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bluetriangle.bluetriangledemo.R
import com.bluetriangle.bluetriangledemo.utils.loadImage

class ProductImageFragment(val image: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productImage = view.findViewById<ImageView>(R.id.product_image)
        productImage.loadImage(image)
    }
}