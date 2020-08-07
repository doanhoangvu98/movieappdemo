package com.shin.movieapp.model.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

const val USER_ID=1L
@Dao
interface UserDao {

    @Insert
    fun insert(user: User):Completable

    @Update
    fun update(user: User):Completable

    @Query("SELECT * FROM user_table WHERE userId = :key")
    fun getUserWithId(key: Long= USER_ID): Flowable<User>

    @Query("SELECT * FROM user_table ORDER BY userId ASC")
    fun getAllUser(): Flowable<List<User>>

    @Query("SELECT * FROM user_table WHERE userId = :key")
    fun checkCurrentUser(key: Long= USER_ID): Maybe<User>

}