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

        val productId = intent.getStringExtra("id") ?: ""
        val productName = intent.getStringExtra("name") ?: ""
        val productPriceInKopecks = intent.getIntExtra("priceInKopecks", 0)
        val productDescription = intent.getStringExtra("longDescription") ?: ""
        val productImageUrl = intent.getStringExtra("imageUrl") ?: ""
        val productCategoryId = intent.getStringExtra("categoryId") ?: ""

        title.text = productName
        price.text = String.format("%,.2f ₽", productPriceInKopecks / 100.0)
        description.text = productDescription

        Glide.with(this)
            .load(productImageUrl)
            .into(image)

        val sizes = listOf("XS", "S", "M", "L", "XL")

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sizes
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        sizeSpinner.adapter = spinnerAdapter

        val currentProduct = Product(
            id = productId,
            name = productName,
            shortDescription = "",
            longDescription = productDescription,
            priceInKopecks = productPriceInKopecks,
            imageUrl = productImageUrl,
            tags = emptyList(),
            sizes = emptyList(),
            categoryId = productCategoryId,
            material = "",
            weight = "",
            season = "",
            countryOfOrigin = ""
        )

        sizeLabel.visibility = View.VISIBLE
        sizeSpinner.visibility = View.VISIBLE

        addToCartButton.setOnClickListener {
            val selectedSize = sizeSpinner.selectedItem.toString()

            CartManager.addToCart(
                currentProduct,
                selectedSize
            )

            Toast.makeText(
                this,
                "Товар добавлен в корзину",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}