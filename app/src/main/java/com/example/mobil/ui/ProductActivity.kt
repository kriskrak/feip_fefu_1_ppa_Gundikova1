package com.example.mobil.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mobil.R
import com.example.mobil.model.CartManager
import com.example.mobil.model.Product

class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val image = findViewById<ImageView>(R.id.image)
        val title = findViewById<TextView>(R.id.title)
        val price = findViewById<TextView>(R.id.price)
        val description = findViewById<TextView>(R.id.description)
        val addToCartButton = findViewById<Button>(R.id.addToCartButton)
        val sizeLabel = findViewById<TextView>(R.id.sizeLabel)
        val sizeSpinner = findViewById<Spinner>(R.id.sizeSpinner)

        val productId = intent.getIntExtra("id", 0)
        val productTitle = intent.getStringExtra("title") ?: ""
        val productPrice = intent.getDoubleExtra("price", 0.0)
        val productDescription = intent.getStringExtra("description") ?: ""
        val productImage = intent.getStringExtra("image") ?: ""
        val productCategory = intent.getStringExtra("category") ?: ""

        title.text = productTitle
        price.text = "$productPrice ₽"
        description.text = productDescription

        Glide.with(this)
            .load(productImage)
            .into(image)

        val currentProduct = Product(
            productId,
            productTitle,
            productPrice,
            productDescription,
            productImage,
            productCategory
        )

        val sizes = listOf("XS", "S", "M", "L", "XL")
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sizes
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sizeSpinner.adapter = spinnerAdapter

        val isClothing = productCategory == "clothing"

        if (isClothing) {
            sizeLabel.visibility = View.VISIBLE
            sizeSpinner.visibility = View.VISIBLE
        } else {
            sizeLabel.visibility = View.GONE
            sizeSpinner.visibility = View.GONE
        }

        addToCartButton.setOnClickListener {
            val selectedSize = if (isClothing) {
                sizeSpinner.selectedItem.toString()
            } else {
                null
            }

            CartManager.addToCart(currentProduct, selectedSize)

            Toast.makeText(
                this,
                "Товар добавлен в корзину",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}