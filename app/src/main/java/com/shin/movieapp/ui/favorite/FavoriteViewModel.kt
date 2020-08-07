package com.shin.movieapp.ui.favorite

import android.app.Application
import android.content.Context
import android.util.EventLog
import android.util.Log
import androidx.lifecycle.*
import com.shin.movieapp.model.Movie.Movie
import com.shin.movieapp.model.Movie.MovieDetails
import com.shin.movieapp.model.favorite.Favorite
import com.shin.movieapp.network.MovieClient
import com.shin.movieapp.repository.favorite.FavoriteRepository
import com.shin.movieapp.repository.movie.MovieDetailsRepository
import com.shin.movieapp.ui.movie.SingleMovieViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository,
                        application: Application) : AndroidViewModel(application){

    var favoriteList = MutableLiveData<List<Favorite>>()
    var isFavorite = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()

    init {
       getFavoriteMovie()
    }

    fun insertFavorite(favorite: Favorite){
        favoriteRepository.insertFavorite(favorite)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.postValue("Add favorite item successful")
                isFavorite.postValue(true)
            }, {
                it.printStackTrace()
            })
    }

    fun deleteFavorite(id: Int){
        favoriteRepository.deleteFavoriteItem(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.postValue("Remove favorite item successful")
                isFavorite.postValue(false)
            }, {
                it.printStackTrace()
            })
    }

    fun getFavoriteMovie(){

        favoriteRepository.getAllFavorite()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                if(!it.isNullOrEmpty()){
                    favoriteList.postValue(it)
                } else{
                    favoriteList.postValue(listOf())
                }
                it?.forEach {

                }
            },{
            })?.let {

            }

    }

    fun checkIsFavorite(id: Int){
        isFavorite.postValue(favoriteRepository.checkItemInList(id))
    }

    fun favoriteEvent(favorite: Favorite){
        if(favoriteRepository.checkItemInList(favorite.movieId)){
            deleteFavorite(favorite.movieId)
        } else {
            insertFavorite(favorite)
        }
    }

}