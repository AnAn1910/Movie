package com.example.movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.data.api.TheMovieDBInterface
import com.example.movie.data.api.TheMovieDbClient
import com.example.movie.data.repository.NetworkState
import com.example.movie.ui.popular_movie.MainActivityViewModel
import com.example.movie.ui.popular_movie.MoviePagedListRepository
import com.example.movie.ui.popular_movie.PopularMoviePagedListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel1: MainActivityViewModel
    lateinit var movieRepository: MoviePagedListRepository
    private lateinit var rv_movie_list: RecyclerView
    val progress_bar_popular: ProgressBar = findViewById(R.id.progress_bar_popular)
    val txt_error_popular: TextView = findViewById(R.id.txt_error_popular)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService : TheMovieDBInterface = TheMovieDbClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel1 = getViewModel()
        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType:Int = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }
        };

        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        viewModel1.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel1.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel1.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel1.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel1.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

}