package com.shin.movieapp.network


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "cf20b218a39b5117caf6b6de0370e9ba"
const val IMAGE_URL = "https://image.tmdb.org/t/p/w185/"
const val WEB_URL = "https://www.themoviedb.org/"
const val FIRST_PAGE = 1
const val ITEM_PER_PAGE = 7

object MovieClient {

    fun getData(): MovieApi {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        var rxAdapter = RxJava2CallAdapterFactory.create()

        return Retrofit.Builder()
            .client(okHttpClient) // http client using for request
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(rxAdapter)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }
}