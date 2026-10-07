package com.example.mobil.data

import com.example.mobil.model.CatalogData
import retrofit2.http.GET

interface CatalogApi {

    @GET("catalog")
    suspend fun getCatalog(): CatalogData
}