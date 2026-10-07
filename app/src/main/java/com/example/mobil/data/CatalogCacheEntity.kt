package com.example.mobil.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog_cache")
data class CatalogCacheEntity(
    @PrimaryKey
    val id: Int = 1,
    val catalogJson: String
)