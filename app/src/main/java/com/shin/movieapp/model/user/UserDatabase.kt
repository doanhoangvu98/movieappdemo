package com.shin.movieapp.model.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shin.movieapp.model.favorite.Favorite
import com.shin.movieapp.model.favorite.FavoriteDao
import com.shin.movieapp.model.reminder.Reminder
import com.shin.movieapp.model.reminder.ReminderDao

@Database(entities = [User::class, Favorite::class, Reminder::class], version = 9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase(){

    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "myDB"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}