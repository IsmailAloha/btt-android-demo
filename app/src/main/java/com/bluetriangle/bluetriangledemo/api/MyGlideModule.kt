package com.bluetriangle.bluetriangledemo.api

import android.content.Context
import android.util.Log
import com.bluetriangle.analytics.okhttp.bttTrack
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.MemoryCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import java.io.File
import java.io.InputStream

@GlideModule
@Excludes(OkHttpLibraryGlideModule::class)
class MyGlideModule : AppGlideModule() {

    private class NoCachingDiskCache:DiskCache {

        override fun get(key: Key?): File? {

            return null
        }

        override fun put(key: Key?, writer: DiskCache.Writer?) {
        }

        override fun delete(key: Key?) {
        }

        override fun clear() {
        }

    }
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDiskCache { NoCachingDiskCache() }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        Log.d("GlideOkHttpSetup", "Registering OkHttp in Glide...")
        val okHttpClient = OkHttpClient.Builder()
            .bttTrack()
            .addInterceptor {
                Log.d(
                    "GlideOkHttpSetup",
                    "Interceptor Called: ${it.request().url}"
                )
                it.proceed(it.request())
            }
            .build()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
    }
}