package com.shin.movieapp.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.shin.movieapp.model.Movie.Movie
import com.shin.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieFragmentViewModel(private val movieRepository: MoviePageList): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movePageList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePageList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean{
        return movePageList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}