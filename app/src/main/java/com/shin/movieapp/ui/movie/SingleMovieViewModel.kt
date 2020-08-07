package com.shin.movieapp.ui.movie

import android.app.Application
import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.shin.movieapp.model.Movie.MovieDetails
import com.shin.movieapp.model.favorite.FavoriteDao
import com.shin.movieapp.model.user.User
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.network.IMAGE_URL
import com.shin.movieapp.repository.movie.MovieDetailsRepository
import com.shin.movieapp.repository.NetworkState
import com.shin.movieapp.repository.favorite.FavoriteRepository
import io.reactivex.disposables.CompositeDisposable


class SingleMovieViewModel (private val movieRepository : MovieDetailsRepository,
                        movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

   fun checkIsFavorite(id: Int){
   }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}