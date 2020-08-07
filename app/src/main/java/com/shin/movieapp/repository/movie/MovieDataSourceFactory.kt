package com.shin.movieapp.repository.movie

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.shin.movieapp.model.Movie.Movie
import com.shin.movieapp.network.MovieApi
import io.reactivex.disposables.CompositeDisposable


class MovieDataSourceFactory(private val movieApi: MovieApi,
                             private val type_get: String,
                             private val compositeDisposable: CompositeDisposable
)
    : DataSource.Factory<Int, Movie>() {

    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(
            movieApi,
            type_get,
            compositeDisposable
        )
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}