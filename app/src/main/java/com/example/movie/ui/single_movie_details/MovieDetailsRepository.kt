package com.example.movie.ui.single_movie_details

import android.app.usage.NetworkStats
import androidx.lifecycle.LiveData
import com.example.movie.data.api.TheMovieDBInterface
import com.example.movie.data.repository.MovieDetailsNetworkDataSource
import com.example.movie.data.repository.NetworkState
import com.example.movie.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.CompletionService

class MovieDetailsRepository (private val apiService: TheMovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails>{
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}