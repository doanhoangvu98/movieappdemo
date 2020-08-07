package com.shin.movieapp.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shin.movieapp.repository.favorite.FavoriteRepository
import java.lang.IllegalArgumentException

class FavoriteModelFactory(private val favoriteRepository: FavoriteRepository,
                           val application: Application
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel(favoriteRepository, application) as T
        }
        return throw IllegalArgumentException("FavoriteModelFactory not generate")
    }
}