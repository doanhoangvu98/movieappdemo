package com.shin.movieapp.ui.popular_movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shin.movieapp.R
import com.shin.movieapp.model.Movie.Movie
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.network.IMAGE_URL
import com.shin.movieapp.repository.NetworkState
import com.shin.movieapp.repository.favorite.FavoriteRepository
import kotlinx.android.synthetic.main.fragment_movie.view.*
import kotlinx.android.synthetic.main.movie_grid_item.view.*
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MoviePageListAdapter(public val context: Context, val viewListType: Int) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_LIST_TYPE = 1
    val MOVIE_GRID_TYPE = 2
    val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if(viewType == MOVIE_LIST_TYPE){
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else if(viewType == MOVIE_GRID_TYPE){
            view = layoutInflater.inflate(R.layout.movie_grid_item, parent, false)
            return MovieGridViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state, parent, false)
            return NetWorkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_LIST_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else if(getItemViewType(position) == MOVIE_GRID_TYPE){
            (holder as MovieGridViewHolder).bind(getItem(position), context)
        }
        else {
            (holder as NetWorkStateItemViewHolder).bind(networkState)
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(movie: Movie?, context: Context){
            itemView.movie_name_text.text = movie?.originalTitle
            itemView.release_date_text.text = movie?.releaseDate
            itemView.overview_text.text = movie?.overview
            itemView.rating_text.text = movie?.voteAverage.toString() + "/10"

            // Check movie exists in favorite to change icon star
            val favoriteDao = UserDatabase.getInstance(context).favoriteDao()
            val favoriteRepository = FavoriteRepository(favoriteDao)
            val isFavorite = favoriteRepository.checkItemInList(movie!!.id)
            if(isFavorite){
                itemView.rating_icon.setImageResource(R.drawable.ic_star_inline_24dp)
            } else {
                itemView.rating_icon.setImageResource(R.drawable.ic_star_outline_white_24dp)
            }
            val moviePosterURL = IMAGE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.movie_poster)

            itemView.setOnClickListener{
                val navController: NavController = Navigation.findNavController(it)
                var bundle = bundleOf(
                    "movie_id" to movie?.id,
                    "movie_title" to movie?.originalTitle
                )
                navController.navigate(R.id.action_movieFragment_to_singleMovieFragment, bundle)
            }
        }
    }

    class MovieGridViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(movie: Movie?, context: Context){
            itemView.movie_name_grid.text = movie?.originalTitle
            val moviePosterURL = IMAGE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.movie_poster_grid)

            // View movie details
            itemView.setOnClickListener{
                val navController: NavController = Navigation.findNavController(it)
                var bundle = bundleOf("movie_id" to movie?.id,
                    "movie_title" to movie?.originalTitle)
                navController.navigate(R.id.action_movieFragment_to_singleMovieFragment, bundle)
            }
        }
    }

    class NetWorkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){
            if(networkState != null && networkState == NetworkState.LOADING){
                itemView.loading.visibility = View.VISIBLE
            } else {
                itemView.loading.visibility = View.GONE
            }

//            if(networkState != null && networkState == NetworkState.ERROR){
//                itemView.error_popular_text.visibility = View.VISIBLE
//                itemView.error_popular_text.text = networkState.msg
//            } else if(networkState != null && networkState == NetworkState.END){
//                itemView.error_popular_text.visibility = View.VISIBLE
//                itemView.error_popular_text.text = networkState.msg
//            } else {
//                itemView.error_popular_text.visibility = View.GONE
//            }
        }
    }

    private fun hasExtraRow() : Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return viewListType
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount()) //remove progress bar
            } else {
                notifyItemInserted(super.getItemCount()) // add progress bar at the end
            }
        } else if(hasExtraRow && previousState != newNetworkState){ // hasExtra and hadExtra is true
            notifyItemChanged(itemCount - 1)
        }
    }
}