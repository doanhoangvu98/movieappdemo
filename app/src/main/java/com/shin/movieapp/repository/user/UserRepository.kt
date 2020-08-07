package com.shin.movieapp.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.shin.movieapp.model.user.User
import com.shin.movieapp.model.user.UserDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class UserRepository(private val userDao: UserDao) {

    fun updateUser(user: User): Completable {
        return userDao.update(user)
    }

    fun checkAndGetCurrentUser(): Flowable<User> {
        val action =
            userDao.checkCurrentUser().switchIfEmpty(Maybe.just(User())).flatMapCompletable {
                if (it.userId == 0L)
                    return@flatMapCompletable userDao.insert(it)
                else return@flatMapCompletable Completable.complete()
            }.andThen(userDao.getUserWithId())

        val user = LiveDataReactiveStreams.fromPublisher(action)
        return action
    }
}