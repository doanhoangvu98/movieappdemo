package com.shin.movieapp.model.favorite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface FavoriteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: Favorite): Completable

    @Query("SELECT * FROM favorite_table ORDER BY favoriteId DESC")
    fun getAll(): Flowable<List<Favorite>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_table WHERE movie_id = :id)")
    fun exists(id: Int): Boolean

    @Query("DELETE FROM favorite_table WHERE movie_id = :id")
    fun delete(id: Int) : Completable

}