package com.github.libliboom.movieposter.io

import android.graphics.Bitmap
import android.util.LruCache

class MemoryCache {

    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

    val cacheSize = maxMemory / 8

    private val memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.byteCount / 1024
        }
    }

    fun get(key: String): Bitmap? {
        return memoryCache.get(key)
    }

    fun add(key: String, value: Bitmap) {
        memoryCache.put(key, value)
    }
}
