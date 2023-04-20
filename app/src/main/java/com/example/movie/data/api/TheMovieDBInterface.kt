package com.example.movie.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import com.example.movie.data.vo.MovieDetails
import io.reactivex.Single

interface TheMovieDBInterface {
    @GET("movie/{299534}")
    fun getMovieDetail(@Path("299534")id:Int):Single<MovieDetails>

}
