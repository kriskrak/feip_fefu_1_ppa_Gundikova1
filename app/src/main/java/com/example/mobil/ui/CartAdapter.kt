package com.example.mobil.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobil.R
import com.example.mobil.model.CartItem

class CartAdapter(private val cartItems: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cartItemImage)
        val title: TextView = view.findViewById(R.id.cartItemTitle)
        val size: TextView = view.findViewById(R.id.cartItemSize)
        val price: TextView = view.findViewById(R.id.cartItemPrice)
        val quantity: TextView = view.findViewById(R.id.cartItemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.title.text = cartItem.product.title
        holder.price.text = "Цена: ${cartItem.product.price} ₽"
        holder.quantity.text = "Количество: ${cartItem.quantity}"

        if (cartItem.size != null) {
            holder.size.text = "Размер: ${cartItem.size}"
        } else {
            holder.size.text = "Размер: —"
        }

        Glide.with(holder.itemView.context)
            .load(cartItem.product.image)
            .into(holder.image)
    }
}