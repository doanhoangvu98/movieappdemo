package com.shin.movieapp.network

import com.shin.movieapp.model.Movie.MovieDetails
import com.shin.movieapp.model.Movie.MovieResponse
import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    fun getMoviePopular(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/top_rated")
    fun getMovieTopRated(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/upcoming")
    fun getMovieUpcoming(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/now_playing")
    fun getMovieNowPlaying(@Query("page") page: Int): Single<MovieResponse>


    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}
