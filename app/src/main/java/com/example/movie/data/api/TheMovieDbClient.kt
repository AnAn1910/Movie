package com.example.movie.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "e88ea4364b09ce93e847b517130e4a08"
const val BASE_URL = "https://api.themoviedb.org/3/movie/550?api_key=e88ea4364b09ce93e847b517130e4a08"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg"

const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

object TheMovieDbClient {

    fun getClient(): TheMovieDBInterface {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("e88ea4364b09ce93e847b517130e4a08", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)
    }
}
