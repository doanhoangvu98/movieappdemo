package com.shin.movieapp.utils

object TypeMovie {

    fun MovieTitle (type_original: String) : String{
        val title = when(type_original) {
            "popular_movie" -> "Popular"
            "upcoming_movie" -> "Upcoming"
            "now_playing_movie" -> "Now Playing"
            "top_rated_movie" -> "Top Rated"
            else -> "Not found title"
        }
        return title
    }
}