package com.shin.movieapp.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.shin.movieapp.model.Movie.Movie
import com.shin.movieapp.network.ITEM_PER_PAGE
import com.shin.movieapp.network.MovieApi
import com.shin.movieapp.repository.movie.MovieDataSource
import com.shin.movieapp.repository.movie.MovieDataSourceFactory
import com.shin.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MoviePageList(private val movieApi: MovieApi,private val type_get: String) {

    lateinit var moviePageList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePageList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>>{
        movieDataSourceFactory =
            MovieDataSourceFactory(
                movieApi,
                type_get,
                compositeDisposable
            )

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(ITEM_PER_PAGE)
            .build()

        moviePageList = LivePagedListBuilder(movieDataSourceFactory, config).build()

        return moviePageList
    }

    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.movieLiveDataSource, MovieDataSource::networkState
        )
    }
}