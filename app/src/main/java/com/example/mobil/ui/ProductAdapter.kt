package com.example.mobil.ui

import android.content.Intent
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
    private val products: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.productImage)
        val title: TextView = view.findViewById(R.id.productTitle)
        val price: TextView = view.findViewById(R.id.productprice)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val product = products[position]

        holder.title.text = product.name

        val priceInRubles = product.priceInKopecks / 100

        holder.price.text =
            "%,d ₽".format(priceInRubles)
                .replace(',', ' ')

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                ProductActivity::class.java
            )

            intent.putExtra("id", product.id)
            intent.putExtra("name", product.name)
            intent.putExtra("priceInKopecks", product.priceInKopecks)
            intent.putExtra("longDescription", product.longDescription)
            intent.putExtra("imageUrl", product.imageUrl)
            intent.putExtra("categoryId", product.categoryId)

            holder.itemView.context.startActivity(intent)
        }
    }
}