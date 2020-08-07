package com.shin.movieapp.model.reminder

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ReminderDao {

    @Insert
    fun insert(reminder: Reminder) : Completable

    @Update
    fun update(reminder: Reminder) : Completable

    @Query("SELECT * FROM reminder_table ORDER BY id ASC")
    fun getAll() : Flowable<List<Reminder>>

    @Query("DELETE FROM reminder_table WHERE id = :id")
    fun delete(id: Int) : Completable

    @Query("SELECT * FROM  reminder_table WHERE movie_id = :id")
    fun checkReminderExist(id: Int) : Reminder

}

