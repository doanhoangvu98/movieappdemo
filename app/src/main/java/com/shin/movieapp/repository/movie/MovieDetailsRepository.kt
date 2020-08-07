package com.shin.movieapp.repository.movie

import androidx.lifecycle.LiveData
import com.shin.movieapp.model.Movie.MovieDetails
import com.shin.movieapp.network.MovieApi
import com.shin.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable


class MovieDetailsRepository (private val api : MovieApi) {

    lateinit var movieDetailsDataSource: MovieDetailsDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsDataSource =
            MovieDetailsDataSource(
                api,
                compositeDisposable
            )
        movieDetailsDataSource.fetchMovieDetails(movieId)

        return movieDetailsDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsDataSource.networkState
    }

}