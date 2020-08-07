package com.shin.movieapp.model.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shin.movieapp.model.Movie.Movie
import io.reactivex.Single

@Entity(tableName = "favorite_table")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    var favoriteId: Int? = null,

    @ColumnInfo(name = "movie_id")
    var movieId: Int,

    @ColumnInfo(name = "movie_title")
    var movieTitle: String
)