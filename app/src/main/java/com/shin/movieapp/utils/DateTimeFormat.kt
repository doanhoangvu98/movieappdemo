package com.shin.movieapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeFormat {

    fun formatDate(year: Int, month: Int, day: Int, hour: Int, minute: Int) : String {
        val c = Calendar.getInstance()
        c.set(year, month, day, hour, minute)
        val pattern = "yyyy-MM-dd HH:mm"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(c.time)
        return date
    }

}