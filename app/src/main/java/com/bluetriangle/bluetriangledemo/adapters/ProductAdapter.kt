package com.bluetriangle.bluetriangledemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.databinding.ListItemProductBinding
import com.bluetriangle.bluetriangledemo.utils.dp
import com.bluetriangle.bluetriangledemo.utils.loadImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ProductAdapter(
    context: Context,
    private val productClickListener: (product: Product) -> Unit
) :
    ListAdapter<Product, RecyclerView.ViewHolder>(ProductDiffCallback()) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(ListItemProductBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        val productViewHolder = holder as? ProductViewHolder
        productViewHolder?.bind(product)
        productViewHolder?.itemView?.setOnClickListener { _ ->
            productClickListener(product)
        }
    }

    class ProductViewHolder(private val binding: ListItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                productName.text = String.format("%d : %s", product.id, product.name)
                productPrice.text = String.format("$%.2f", product.price)
                productImage.loadImage(product.image)
            }
        }
    }
}

private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
