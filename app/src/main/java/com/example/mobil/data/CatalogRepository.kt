package com.example.mobil.data

import com.example.mobil.model.CatalogData
import com.google.gson.Gson

class CatalogRepository(
    private val api: CatalogApi,
    private val cacheDao: CatalogCacheDao
) {

    private val gson = Gson()

    suspend fun getCachedCatalog(): CatalogData? {

        val cache =
            cacheDao.getCatalog()
                ?: return null

        return gson.fromJson(
            cache.catalogJson,
            CatalogData::class.java
        )
    }

    suspend fun refreshCatalog(): CatalogData {

        val catalog =
            api.getCatalog()

        val json =
            gson.toJson(catalog)

        cacheDao.saveCatalog(
            CatalogCacheEntity(
                catalogJson = json
            )
        )

        return catalog
    }
}