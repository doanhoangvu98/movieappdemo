package com.shin.movieapp.repository.favorite

import com.shin.movieapp.model.favorite.Favorite
import com.shin.movieapp.model.favorite.FavoriteDao
import io.reactivex.Completable
import io.reactivex.Flowable

class FavoriteRepository(private val favoriteDao: FavoriteDao){

    fun insertFavorite(favorite: Favorite) : Completable{
        return favoriteDao.insert(favorite)
    }

    fun getAllFavorite() : Flowable<List<Favorite>> {
        return favoriteDao.getAll()
    }

    fun checkItemInList(id: Int): Boolean {
        return favoriteDao.exists(id)
    }

    fun deleteFavoriteItem(id: Int) : Completable {
        return favoriteDao.delete(id)
    }
}