package com.example.mobil.data

import androidx.room.Entity

@Entity(
    tableName = "cart",
    primaryKeys = ["productId", "sizeId"]
)
data class CartEntity(
    val productId: String,
    val sizeId: String,
    val quantity: Int
)