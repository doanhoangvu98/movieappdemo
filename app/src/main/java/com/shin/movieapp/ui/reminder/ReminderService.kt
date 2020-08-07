package com.shin.movieapp.ui.reminder

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.shin.movieapp.MainActivity
import com.shin.movieapp.utils.NotificationUtils


class ReminderService : Service(){
    companion object {
        lateinit var r: Ringtone
    }

    var id: Int = 0
    var isRunning: Boolean = false

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        var state: String = intent!!.getStringExtra("extra")
        var movieId = intent!!.getIntExtra("movieId", 0)
        var posterUrl = intent!!.getStringExtra("posterUrl")
        var movieTitle = intent!!.getStringExtra("movieTitle")

        if(intent != null){
//            playAlarm()
            showNotification(movieId, posterUrl, movieTitle)
        } else {
            stopMyService()
        }
        return START_STICKY
    }

    private fun playAlarm(){
        var alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if(alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        }
        r = RingtoneManager.getRingtone(baseContext, alarmUri)
        r.play()
    }

    private fun showNotification(movieId: Int, posterUrl: String, movieTitle: String){
        val notificationUtils = NotificationUtils(this)
        val notification =
            notificationUtils.getNotificationBuilder(movieId, posterUrl, movieTitle).build()
        notificationUtils.getManager().notify(150, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.isRunning = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun stopMyService() {
        stopForeground(true)
        stopSelf()
    }

}