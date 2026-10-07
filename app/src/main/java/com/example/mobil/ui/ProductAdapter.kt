package com.example.mobil.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobil.R
import com.example.mobil.model.Product

class ProductAdapter(
    private val products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        val image: ImageView =
            view.findViewById(R.id.productImage)

        val title: TextView =
            view.findViewById(R.id.productTitle)

        val price: TextView =
            view.findViewById(R.id.productprice)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.product_item,
                parent,
                false
            )

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {

        val product = products[position]

        holder.title.text = product.name

        val priceInRubles =
            product.priceInKopecks / 100

        holder.price.text =
            "%,d ₽".format(priceInRubles)
                .replace(',', ' ')

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }
}