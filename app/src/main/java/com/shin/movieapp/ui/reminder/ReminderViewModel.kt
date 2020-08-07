package com.shin.movieapp.ui.reminder

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shin.movieapp.model.reminder.Reminder
import com.shin.movieapp.repository.reminder.ReminderRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReminderViewModel(private val reminderRepository: ReminderRepository) : ViewModel(){

    var message = MutableLiveData<String>()
    var listReminder = MutableLiveData<List<Reminder>>()

    init {
        getAllReminder()
    }

    fun getAllReminder(){
        reminderRepository.getAllReminder()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listReminder.postValue(it)
            }, {
                it.printStackTrace()
            })
    }

    fun insertReminder(reminder: Reminder){
        reminderRepository.insertReminder(reminder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.postValue("Create reminder successful")
            }, {
                it.printStackTrace()
            })
    }

    fun deleteReminder(id: Int){
        reminderRepository.deleteReminder(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.postValue("Remove reminder successful")
            }, {
                it.printStackTrace()
            })
    }

    fun updateReminder(reminder: Reminder){
        reminderRepository.updateReminder(reminder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.postValue("Update reminder successful")
            }, {
                it.printStackTrace()
            })
    }

    fun eventReminder(reminder: Reminder){
        val oldReminder = reminderRepository.checkReminderExists(reminder.movieId)
        if(oldReminder == null){
            insertReminder(reminder)
        } else {
            val update = Reminder(oldReminder.id, oldReminder.movieId, reminder.reminderTime)
            updateReminder(update)
        }
    }
}