package com.shin.movieapp.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shin.movieapp.R
import com.shin.movieapp.model.favorite.Favorite
import com.shin.movieapp.network.IMAGE_URL
import com.shin.movieapp.network.MovieClient
import com.shin.movieapp.repository.movie.MovieDetailsRepository
import com.shin.movieapp.ui.movie.SingleMovieViewModel
import kotlinx.android.synthetic.main.favorite_list_item.view.*
import java.util.*

class FavoriteDataAdapter(public val context: Context, private val favorites: List<Favorite>):
    RecyclerView.Adapter<FavoriteDataAdapter.ViewHolder>(), Filterable{

    var favoriteFilterList: List<Favorite> = favorites

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bind(item: Favorite, context: Context){
            val apiService = MovieClient.getData()
            var movieRepository = MovieDetailsRepository(apiService)
            var viewModel = SingleMovieViewModel(movieRepository, item.movieId)

            // Get movie with movie id
            viewModel.movieDetails.observe(view.context as LifecycleOwner, Observer {
                itemView.release_date_text.text = it.release_date
                itemView.rating_text.text = it.vote_average.toString() + "/10"
                itemView.overview_text.text = it.overview
                itemView.movie_name_text.text = it.original_title
                var moviePosterUrl = IMAGE_URL + it.poster_path
                Glide.with(context).load(moviePosterUrl).into(itemView.movie_poster)

                val movie = it

                // View movie details
                itemView.setOnClickListener{
                    val navController: NavController = Navigation.findNavController(it)
                    var bundle = bundleOf("movie_id" to movie.id,
                        "movie_title" to movie.original_title)
                    navController.navigate(R.id.action_favoriteFragment_to_singleMovieFragment, bundle)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = favoriteFilterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(favoriteFilterList[position], context)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    favoriteFilterList = favorites
                } else {
                    var resultList: MutableList<Favorite> = arrayListOf()
                    for (row in favorites){
                        if(row.movieTitle!!.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))){
                            resultList.add(row)
                        }
                    }
                    favoriteFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = favoriteFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                favoriteFilterList = (results?.values as List<Favorite>).toMutableList()
                notifyDataSetChanged()
            }

        }
    }
}