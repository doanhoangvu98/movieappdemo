package com.shin.movieapp.ui.user

import android.app.Application
import android.content.Intent
import android.media.Image
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.shin.movieapp.R
import com.shin.movieapp.model.user.User
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.repository.user.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat

class UserViewModel(private var userRepository: UserRepository, application: Application) : AndroidViewModel(application) {

    var user = MutableLiveData<User>()
    var radio_checked = MutableLiveData<Int>()

    init {
       userRepository.checkAndGetCurrentUser()
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe({
                user.value=it
                radio_checked.postValue(if(it.gender == "Male") R.id.male_radio_button else R.id.female_radio_button)
       },{
            it.printStackTrace()
       },{

       })
    }

    fun updateUser( birthday:String, male:Boolean, imagePath: String ){
        user.value!!.birthDay = birthday
        user.value!!.gender = if (male) "Male" else "Female"
        if(imagePath != "null") {
            user.value!!.profileImg = imagePath
        } else {
            user.value!!.profileImg
        }
        userRepository.updateUser(user.value!!).subscribe()
    }
}