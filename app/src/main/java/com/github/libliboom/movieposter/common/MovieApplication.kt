package com.github.libliboom.movieposter.common

import android.app.Application
import android.content.Context

class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        lateinit var context: Context
    }
}
