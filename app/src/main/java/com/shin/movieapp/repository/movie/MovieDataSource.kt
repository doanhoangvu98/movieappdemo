package com.shin.movieapp.repository.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.shin.movieapp.model.Movie.Movie
import com.shin.movieapp.network.FIRST_PAGE
import com.shin.movieapp.network.MovieApi
import com.shin.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val movieApi: MovieApi, private val type_get: String, private val compositeDisposable:
CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val type = when(type_get){
        "popular_movie" -> movieApi.getMoviePopular(page)
        "top_rated_movie" -> movieApi.getMovieTopRated(page)
        "upcoming_movie" -> movieApi.getMovieUpcoming(page)
        "now_playing_movie" -> movieApi.getMovieNowPlaying(page)
        else -> movieApi.getMoviePopular(page)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            type
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        Log.i("MovieDataSource", "Init load")
                        callback.onResult(it.movieList, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSourceRepo", it.message)
                    }
                )
        )
    }

    // Next page
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            movieApi.getMoviePopular(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages >= params.key){
                            Log.i("MovieDataSource", "Load after page ${params.key+1}")
                            callback.onResult(it.movieList, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.END)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSourceRepo", it.message)
                    }
                )
        )
    }

    // Previous page
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }

}