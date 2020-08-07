package com.shin.movieapp.ui.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shin.movieapp.repository.user.UserRepository
import java.lang.IllegalArgumentException

//generate userviewmodel only
class UserViewModelFactory (val repository: UserRepository,val application: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(repository,application) as T
        }
        return  throw IllegalArgumentException("Class requested is not userviewmodel")
    }
}