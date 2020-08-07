package com.shin.movieapp.ui.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
//        Log.i("Broadcast","recieved ${intent?.extras.toString()}")

        var getResult: String = intent!!.getStringExtra("extra")
        var movieId = intent!!.getIntExtra("movieId", 0)
        var posterUrl = intent!!.getStringExtra("posterUrl")
        var movieTitle = intent!!.getStringExtra("movieTitle")

        var service_intent = Intent(context, ReminderService::class.java)

        service_intent.putExtra("movieId", movieId)
        service_intent.putExtra("posterUrl", posterUrl)
        service_intent.putExtra("movieTitle", movieTitle)
//        service_intent.putExtra("extra", getResult)
        context!!.startService(service_intent)
    }

}