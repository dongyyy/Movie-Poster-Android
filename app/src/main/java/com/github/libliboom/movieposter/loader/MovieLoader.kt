package com.github.libliboom.movieposter.loader

import com.github.libliboom.movieposter.data.Movie
import io.reactivex.rxjava3.core.Observable
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

class MovieLoader {

    lateinit var movie: Movie

    fun load() {
        val jsonString =
            """
            {
              "title": "Civil War",
              "image": [
                "http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg?type=m665_443_2",
                "http://movie.phinf.naver.net/20151127_84/1448585272016tiBsF_JPEG/movie_image.jpg?type=m665_443_2",
                "http://movie.phinf.naver.net/20151125_36/1448434523214fPmj0_JPEG/movie_image.jpg?type=m665_443_2"
              ]
            }
            """.trimIndent()

        val jsonObj = JSONObject(jsonString)
        val title = jsonObj.get("title") as String
        val arr: JSONArray = jsonObj.getJSONArray("image")
        val images = mutableListOf<String>()
        for (idx in 0 until arr.length()) {
            images.add(arr[idx] as String)
        }

        movie = Movie(title, images)
    }

    private val client = HttpClientBuilder.create().build()

    fun getRequest(index: Int): Observable<HttpGet> {
        val request = HttpGet(movie.image[index])
        return Observable.just(request)
    }

    fun getObservableInputStream(request: HttpGet): Observable<InputStream> {
        val response = client.execute(request)
        val entity = response.entity
        return Observable.just(entity.content)
    }
}
