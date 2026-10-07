package com.example.mobil.data

import android.content.Context
import com.example.mobil.model.CatalogData
import com.google.gson.Gson

class CatalogRepository(
    private val context: Context
) {

    fun loadCatalog(): CatalogData {
        val json = context.assets
            .open("products.json")
            .bufferedReader()
            .use { it.readText() }

        return Gson().fromJson(json, CatalogData::class.java)
    }
}