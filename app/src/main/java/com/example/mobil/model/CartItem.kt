package com.example.mobil.model

data class CartItem(
    val product: Product,
    val size: String?,
    var quantity: Int
)