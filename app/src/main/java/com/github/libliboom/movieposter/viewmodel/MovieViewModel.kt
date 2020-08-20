package com.github.libliboom.movieposter.viewmodel

import android.graphics.Bitmap
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.libliboom.movieposter.io.FileManager
import com.github.libliboom.movieposter.loader.MovieLoader
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieViewModel : ViewModel() {

    private val movieLoader = MovieLoader()
    private val fileManager = FileManager()

    private lateinit var mainHandler: Handler
    private lateinit var downloadStartCallback: () -> Unit
    private lateinit var downloadFinishCallback: () -> Unit

    private var _downloadedSize = MutableLiveData<Long>()
    val downloadedSize = _downloadedSize
    private var _currentPath = MutableLiveData<String>()
    val currentPath = _currentPath

    var currentPosterIndex = 0
        set(value) {
            field = if (value >= movieLoader.movie.image.size) 0 else value
        }

    fun setHandler(handler: Handler) {
        mainHandler = handler
    }

    fun setCompleteCallback(
        startCallback: () -> Unit,
        finishCallback: () -> Unit
    ) {
        this.downloadStartCallback = startCallback
        this.downloadFinishCallback = finishCallback
    }

    fun loadMovie() {
        movieLoader.load()
    }

    fun fetchMovie() {
        downloadStartCallback()
        movieLoader.getRequest(currentPosterIndex++)
            .subscribeOn(Schedulers.io())
            .flatMap { movieLoader.getObservableInputStream(it) }
            .doOnComplete { downloadFinishCallback() }
            .subscribe { istream ->
                fileManager.saveAsFile(
                    istream,
                    movieLoader.movie.title,
                    currentPosterIndex,
                    ::updatePath,
                    ::updateProgress
                )
            }
    }

    fun getTitle() = movieLoader.movie.title

    fun getBitmap(path: String): Bitmap? {
        return fileManager.getBitmap(path)
    }

    fun next() {
        fetchMovie()
    }

    private fun updatePath(updatedPath: String) {
        mainHandler.post {
            _currentPath.value = updatedPath
        }
    }

    private fun updateProgress(bytes: Long) {
        mainHandler.post {
            _downloadedSize.value = bytes
        }
    }
}
