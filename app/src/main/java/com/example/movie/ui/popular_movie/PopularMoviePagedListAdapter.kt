@file:Suppress("DEPRECATION")

package com.example.movie.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.data.api.POSTER_BASE_URL
import com.example.movie.data.repository.NetworkState
import com.example.movie.data.vo.Movie
import com.example.movie.ui.single_movie_details.SingleMovie

class PopularMoviePagedListAdapter(public val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow():Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    companion object {
        private val MovieDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else{
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val cv_movie_title: TextView = view.findViewById(R.id.cv_movie_title)
        val cv_movie_release_date: TextView = view.findViewById(R.id.cv_movie_release_date)
        val cv_iv_movie_poster: ImageView = view.findViewById(R.id.cv_iv_movie_poster)

        fun bind(movie: Movie?,context: Context){
            cv_movie_title.text = movie?.title
            cv_movie_release_date.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(cv_iv_movie_poster)

            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val progress_bar_item: ProgressBar = view.findViewById(R.id.progress_bar_item)
        val error_msg_item: TextView = view.findViewById(R.id.error_msg_item)

        fun bind(networkState: NetworkState?){
            if(networkState != null && networkState == NetworkState.LOADING){
                progress_bar_item.visibility = View.VISIBLE;
            }
            else{
                progress_bar_item.visibility = View.GONE;
            }
            if (networkState != null && networkState == NetworkState.ERROR){
                error_msg_item.visibility = View.VISIBLE;
                error_msg_item.text = networkState.msg;
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST){
                error_msg_item.visibility = View.VISIBLE;
                error_msg_item.text = networkState.msg;
            }
            else{
                error_msg_item.visibility = View.GONE;
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState: NetworkState ?= this.networkState
        val hadExtraRow:Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow:Boolean = hasExtraRow()

        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        }else if (hasExtraRow && previousState != networkState){
            notifyItemChanged(itemCount - 1)
        }
    }
}

