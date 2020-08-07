package com.shin.movieapp.repository.reminder

import com.shin.movieapp.model.reminder.Reminder
import com.shin.movieapp.model.reminder.ReminderDao
import io.reactivex.Completable
import io.reactivex.Flowable

class ReminderRepository (private val reminderDao: ReminderDao){

    fun insertReminder(reminder: Reminder) : Completable {
        return reminderDao.insert(reminder)
    }

    fun getAllReminder() : Flowable<List<Reminder>> {
        return reminderDao.getAll()
    }

    fun deleteReminder(id: Int) : Completable {
        return reminderDao.delete(id)
    }

    fun checkReminderExists(id: Int) : Reminder {
        return reminderDao.checkReminderExist(id)
    }

    fun updateReminder(reminder: Reminder) : Completable{
        return reminderDao.update(reminder)
    }
}