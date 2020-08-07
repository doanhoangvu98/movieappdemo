package com.shin.movieapp.ui.reminder

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shin.movieapp.repository.reminder.ReminderRepository

class ReminderModelFactory(private val reminderRepository: ReminderRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReminderViewModel::class.java)){
            return ReminderViewModel(reminderRepository) as T
        }
        return throw IllegalArgumentException("ReminderViewModel not generate")
    }

}