package com.example.mobil.model

object CartManager {
    val cartItems = mutableListOf<CartItem>()

    fun addToCart(product: Product, size: String?) {
        val existingItem = cartItems.find {
            it.product.id == product.id && it.size == size
        }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            cartItems.add(
                CartItem(
                    product = product,
                    size = size,
                    quantity = 1
                )
            )
        }
    }
}