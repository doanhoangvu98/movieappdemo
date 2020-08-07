package com.shin.movieapp.model.user

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "user_name")
    var userName: String = "No name",

    @ColumnInfo(name = "birth_day")
    var birthDay: String = formatDate(Date()),

    @ColumnInfo(name = "mail")
    var mail: String = "email",

    @ColumnInfo(name = "gender")
    var gender: String="Male",

    @ColumnInfo(name ="profile_image")
    var profileImg: String = "@drawable/profile_image"
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

fun formatDate(date: Date): String{
    val pattern = "yyyy-MM-dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    val date: String = simpleDateFormat.format(date)
    Log.i("Init date", date)
    return date
}