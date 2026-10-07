package com.example.mobil.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL =
        "https://fefu2026spring.deploy.feip.dev/"

    private const val TOKEN =
        "Cmt7wdwFgDIi1_SRX8hlJIExs0jJKPr4axflLpExAxM"

    private val authInterceptor =
        Interceptor { chain ->

            val request = chain.request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer $TOKEN"
                )
                .build()

            chain.proceed(request)
        }

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val client =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    val api: CatalogApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(CatalogApi::class.java)
}