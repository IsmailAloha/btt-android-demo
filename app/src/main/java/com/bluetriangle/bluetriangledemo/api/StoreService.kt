package com.bluetriangle.bluetriangledemo.api

import android.util.Log
import com.bluetriangle.analytics.Tracker
import com.bluetriangle.analytics.okhttp.BlueTriangleOkHttpInterceptor
import com.bluetriangle.bluetriangledemo.data.Cart
import com.bluetriangle.bluetriangledemo.data.CartItem
import com.bluetriangle.bluetriangledemo.data.Product
import com.squareup.moshi.Json
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import javax.inject.Singleton

interface StoreService {

    @GET("api/product/")
    suspend fun listProducts(): List<Product>

    @GET("api/cart/{id}/")
    suspend fun getCart(@Path("id") cartId: Long): Cart

    @FormUrlEncoded
    @POST("api/cart/")
    suspend fun createCart(@Field("cartitem_set") cartItemIds: List<Long>?): Cart

    @FormUrlEncoded
    @POST("api/item/")
    suspend fun addToCart(
        @Field("product") productId: Long,
        @Field("quantity") quantity: Long,
        @Field("price") price: Double,
        @Field("cart") cartId: Long,
    ): CartItem

    @FormUrlEncoded
    @PATCH("api/item/{cart_item_id}/")
    suspend fun updateQuantity(
        @Path("cart_item_id") cartItemId: Long,
        @Field("quantity") quantity: Long,
    )

    @DELETE("api/item/{cart_item_id}/")
    suspend fun deleteCartItem(@Path("cart_item_id") cartItemId: Long): Response<Unit>

    @DELETE("api/cart/{cart_item_id}/checkout/")
    suspend fun checkout(@Path("cart_item_id") cartItemId: Long): Response<Unit>

    companion object {
        private const val BASE_URL = "http://34.31.235.63:8000/btriangle/"

        fun create(): StoreService {
            Log.d("StoreServiceLog", "Initailizing...")
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(logger)

            if(Tracker.instance != null) {
                clientBuilder.addInterceptor(BlueTriangleOkHttpInterceptor(Tracker.instance!!.configuration))
            }

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(StoreService::class.java)
        }
    }
}

@InstallIn(SingletonComponent::class)
@Module
object StoreServiceModule {
    @Provides
    @Singleton
    fun provideStoreService(): StoreService {
        return StoreService.create()
    }
}