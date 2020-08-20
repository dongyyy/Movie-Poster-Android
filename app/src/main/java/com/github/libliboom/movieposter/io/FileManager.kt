package com.github.libliboom.movieposter.io

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.github.libliboom.movieposter.common.MovieApplication
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

class FileManager {

    private val context by lazy { MovieApplication.context }

    private val cache by lazy { MemoryCache() }

    fun saveAsFile(
        istream: InputStream,
        root: String,
        index: Int,
        updatePath: (String) -> Unit,
        updateProgress: (Long) -> Unit
    ) {
        val path = "${context.cacheDir}/${root.trim().toLowerCase()}-$index.jpg"
        val ostream = FileOutputStream(File(path))

        var bytesCopied: Long = 0
        val disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
            .subscribe {
                updateProgress(bytesCopied)
            }

        val buffer = ByteArray(8192)
        var bytes = istream.read(buffer)
        while (bytes >= 0) {
            ostream.write(buffer, 0, bytes)
            bytesCopied += bytes
            bytes = istream.read(buffer)
        }

        updatePath(path)

        if (disposable.isDisposed.not()) {
            updateProgress(bytesCopied)
            disposable.dispose()
        }
    }

    fun getBitmap(path: String): Bitmap? {
        return try {
            if (cache.get(path) != null) {
                cache.get(path)
            } else {
                val bitmap = BitmapFactory.decodeFile(path)
                cache.add(path, bitmap)
                bitmap
            }
        } catch (e: Exception) {
            null
        }
    }
}
