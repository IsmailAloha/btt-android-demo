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
import com.bluetriangle.bluetriangledemo.utils.dp
import com.bluetriangle.bluetriangledemo.utils.loadImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class CartItemAdapter(
    context: Context,
    private val cartItemRemoveCallback: (cartItem: CartItem) -> Unit,
    private val cartMinusQuantityCallback: (cartItem: CartItem) -> Unit,
    private val cartPlusQuantityCallback: (cartItem: CartItem) -> Unit
) :
    ListAdapter<CartItem, RecyclerView.ViewHolder>(CartItemDiffCallback()) {
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartItemViewHolder(
            ListItemCartItemBinding.inflate(layoutInflater, parent, false),
            cartItemRemoveCallback,
            cartMinusQuantityCallback,
            cartPlusQuantityCallback
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem = getItem(position)
        val cartItemViewHolder = holder as? CartItemViewHolder
        cartItemViewHolder?.bind(cartItem)
    }

    class CartItemViewHolder(
        private val binding: ListItemCartItemBinding,
        private val cartItemRemoveCallback: (cartItem: CartItem) -> Unit,
        private val cartMinusQuantityCallback: (cartItem: CartItem) -> Unit,
        private val cartPlusQuantityCallback: (cartItem: CartItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.apply {
                productQuantity.text = cartItem.quantity.toString()
                lineItemTotal.text = String.format("$%.2f", cartItem.total)
                cartItem.productReference?.let { product ->
                    productName.text = product.name
                    productImage.loadImage(product.image)
                } ?: run {
                    Glide.with(productImage).clear(productImage)
                }
                quantityPlus.setOnClickListener {
                    cartPlusQuantityCallback(cartItem)
                }
                quantityMinus.setOnClickListener {
                    cartMinusQuantityCallback(cartItem)
                }
                removeButton.setOnClickListener { _ ->
                    cartItemRemoveCallback(cartItem)
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
