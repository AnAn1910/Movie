@file:Suppress("DEPRECATION")

package com.example.movie.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.lifecycle.Transformations
import com.example.movie.data.api.POST_PER_PAGE
import com.example.movie.data.api.TheMovieDBInterface
import com.example.movie.data.repository.MovieDataSource
import com.example.movie.data.repository.MovieDataSourceFactory
import com.example.movie.data.repository.NetworkState
import com.example.movie.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService : TheMovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>>{
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config:PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }

}