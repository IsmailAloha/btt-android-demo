package com.bluetriangle.bluetriangledemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.data.Product
import com.bluetriangle.bluetriangledemo.databinding.ListItemCartItemBinding
import com.bluetriangle.bluetriangledemo.databinding.ListItemProductBinding
import com.bumptech.glide.Glide

class CartItemAdapter(context: Context): ListAdapter<CartItem, RecyclerView.ViewHolder>(CartItemDiffCallback()) {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartItemViewHolder(ListItemCartItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem = getItem(position)
        val cartItemViewHolder = holder as? CartItemViewHolder
        cartItemViewHolder?.bind(cartItem)
    }

    class CartItemViewHolder(private val binding: ListItemCartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.apply {
                productQuantity.text = "Qty: ${cartItem.quantity}"
                lineItemTotal.text = "$${cartItem.total}"
                cartItem.productReference?.let { product ->
                    productName.text = product.name
                    Glide.with(productImage).load(product.image).into(productImage)
                } ?: run {
                    Glide.with(productImage).clear(productImage)
                }
            }
        }
    }
}

private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {

    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}
