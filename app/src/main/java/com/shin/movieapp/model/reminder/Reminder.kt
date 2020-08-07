package com.shin.movieapp.model.reminder

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "reminder_table")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "movie_id")
    var movieId: Int,

    @ColumnInfo(name = "reminder_time")
    var reminderTime: String = formatDate(Date())
)

fun formatDate(date: Date): String{
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val simpleDateFormat = SimpleDateFormat(pattern)
    val date: String = simpleDateFormat.format(date)
    Log.i("Init date reminder", date)
    return date
}