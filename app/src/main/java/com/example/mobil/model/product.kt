package com.example.mobil.model

data class CatalogData(
    val categories: List<Category>,
    val items: List<Product>
)

data class Category(
    val id: String,
    val name: String
)

data class Product(
    val id: String,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val priceInKopecks: Int,
    val imageUrl: String,
    val tags: List<String>,
    val sizes: List<ProductSize>,
    val categoryId: String,
    val material: String,
    val weight: String,
    val season: String,
    val countryOfOrigin: String
)

data class ProductSize(
    val id: String,
    val name: String
)