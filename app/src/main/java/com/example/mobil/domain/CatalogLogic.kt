package com.example.mobil.domain

import com.example.mobil.model.CatalogData
import com.example.mobil.model.Product

object CatalogLogic {

    fun filterProducts(
        categoryName: String,
        catalog: CatalogData
    ): List<Product> {

        return if (categoryName == "Новинки") {

            catalog.items.filter { product ->
                "New" in product.tags
            }

        } else {

            val category =
                catalog.categories.find {
                    it.name == categoryName
                }

            if (category == null) {
                emptyList()
            } else {
                catalog.items.filter { product ->
                    product.categoryId == category.id
                }
            }
        }
    }

    fun formatPrice(
        priceInKopecks: Int
    ): String {

        val rubles =
            priceInKopecks / 100

        val formatted =
            rubles
                .toString()
                .reversed()
                .chunked(3)
                .joinToString(" ")
                .reversed()

        return "$formatted ₽"
    }

    fun calculateTotal(
        priceInKopecks: Int,
        quantity: Int
    ): Int {

        return priceInKopecks * quantity
    }

    fun isValidName(
        name: String
    ): Boolean {

        return name.trim().isNotEmpty()
    }

    fun isSimpleEmailValid(
        email: String
    ): Boolean {

        val value =
            email.trim()

        return value.contains("@") &&
                value.substringAfter("@").contains(".") &&
                !value.startsWith("@") &&
                !value.endsWith("@")
    }
}