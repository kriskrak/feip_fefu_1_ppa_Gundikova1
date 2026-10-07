package com.example.mobil.model

import com.example.mobil.data.CartEntity

data class CartDisplayItem(
    val cartEntity: CartEntity,
    val product: Product,
    val sizeName: String
)