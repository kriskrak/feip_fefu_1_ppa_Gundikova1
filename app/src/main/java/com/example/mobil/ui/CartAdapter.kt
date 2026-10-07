package com.example.mobil.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobil.R
import com.example.mobil.model.CartDisplayItem

class CartAdapter(
    private var items: List<CartDisplayItem>,
    private val onIncrease: (CartDisplayItem) -> Unit,
    private val onDecrease: (CartDisplayItem) -> Unit,
    private val onDelete: (CartDisplayItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val image: ImageView =
            view.findViewById(R.id.cartItemImage)

        val title: TextView =
            view.findViewById(R.id.cartItemTitle)

        val size: TextView =
            view.findViewById(R.id.cartItemSize)

        val price: TextView =
            view.findViewById(R.id.cartItemPrice)

        val quantity: TextView =
            view.findViewById(R.id.cartItemQuantity)

        val increaseButton: Button =
            view.findViewById(R.id.increaseButton)

        val decreaseButton: Button =
            view.findViewById(R.id.decreaseButton)

        val deleteButton: Button =
            view.findViewById(R.id.deleteButton)
    }

    fun submitItems(
        newItems: List<CartDisplayItem>
    ) {

        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.cart_item,
                    parent,
                    false
                )

        return CartViewHolder(view)
    }

    override fun getItemCount(): Int =
        items.size

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {

        val item =
            items[position]

        holder.title.text =
            item.product.name

        holder.size.text =
            "Размер: ${item.sizeName}"

        holder.quantity.text =
            item.cartEntity.quantity.toString()

        val totalInKopecks =
            item.product.priceInKopecks *
                    item.cartEntity.quantity

        val rubles =
            totalInKopecks / 100

        holder.price.text =
            "%,d ₽".format(rubles)
                .replace(',', ' ')

        Glide.with(
            holder.itemView.context
        )
            .load(item.product.imageUrl)
            .into(holder.image)

        holder.increaseButton.setOnClickListener {
            onIncrease(item)
        }

        holder.decreaseButton.setOnClickListener {
            onDecrease(item)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(item)
        }
    }
}