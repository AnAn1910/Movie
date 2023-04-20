package com.example.movie.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.api.POSTER_BASE_URL
import com.example.movie.data.api.TheMovieDBInterface
import com.example.movie.data.api.TheMovieDbClient
import com.example.movie.data.repository.NetworkState
import com.example.movie.data.vo.MovieDetails
import java.text.NumberFormat
import java.util.*
import androidx.lifecycle.ViewModelProvider


class SingleMovie : AppCompatActivity(), LifecycleOwner {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : TheMovieDBInterface = TheMovieDbClient.getClient()

        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer{
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer{
            val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
            val txt_error = findViewById<TextView>(R.id.txt_error)
            progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI(it: MovieDetails){
        val movie_title = findViewById<TextView>(R.id.movie_title)
        val movie_tagline = findViewById<TextView>(R.id.movie_tagline)
        val movie_release_date = findViewById<TextView>(R.id.movie_release_date)
        val movie_rating = findViewById<TextView>(R.id.movie_rating)
        val movie_runtime = findViewById<TextView>(R.id.movie_runtime)
        val movie_overview = findViewById<TextView>(R.id.movie_overview)
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        val movie_budget = findViewById<TextView>(R.id.movie_budget)
        val movie_revenue = findViewById<TextView>(R.id.movie_revenue)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val iv_movie_poster = findViewById<ImageView>(R.id.iv_movie_poster)
        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster)
    }

    private fun getViewModel(movieId:Int): SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}