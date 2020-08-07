package com.shin.movieapp.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.shin.movieapp.MainActivity
import com.shin.movieapp.R
import com.shin.movieapp.network.IMAGE_URL
import com.shin.movieapp.ui.movie.SingleMovieFragment
import com.shin.movieapp.ui.setting.SettingFragment
import java.net.URL


class  NotificationUtils(base: Context) : ContextWrapper(base) {

    val MYCHANNEL_ID = "com.shin.movieapp"
    val MYCHANNEL_NAME = "App Alert Notification"

    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    // Create channel for Android version 26+
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val channel = NotificationChannel(MYCHANNEL_ID, MYCHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)

        getManager().createNotificationChannel(channel)
    }

    // Get Manager
    fun getManager() : NotificationManager {
        if (manager == null) manager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        return manager as NotificationManager
    }

    fun getNotificationBuilder(movieId: Int, posterUrl: String, movieTitle: String): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra("movieId", movieId)
        intent.putExtra("movieTitle", movieTitle)

        val pendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)

//        var largeImg = UrlToBitmap.getBitmapFromURL(IMAGE_URL + posterUrl)

        return NotificationCompat.Builder(applicationContext, MYCHANNEL_ID)
            .setContentTitle("Movie app")
            .setPriority(1)
            .setContentText("You have a reminder now. Click to see it!")
            .setSmallIcon(R.drawable.ic_star_outline_white_24dp)
//            .setLargeIcon(image)
            .setColor(Color.YELLOW)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
    }
}