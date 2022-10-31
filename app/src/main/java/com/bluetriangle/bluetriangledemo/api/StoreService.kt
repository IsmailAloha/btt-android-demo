package com.bluetriangle.bluetriangledemo.api

import com.bluetriangle.analytics.Tracker
import com.bluetriangle.analytics.okhttp.BlueTriangleOkHttpInterceptor
import com.bluetriangle.bluetriangledemo.data.Product
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface StoreService {

    @GET("api/product/")
    suspend fun listProducts(): List<Product>

    companion object {
        private const val BASE_URL = "https://development.mms.mobelux.com/btriangle/"

        fun create(): StoreService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(BlueTriangleOkHttpInterceptor(Tracker.instance!!.configuration))
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(StoreService::class.java)
        }
    }
}