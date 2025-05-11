package com.example.workmanagerkotlin.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.workmanagerkotlin.MainActivity
import com.example.workmanagerkotlin.R

class ForegroundOfferService: Service() {

    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()
        handlerThread = HandlerThread("Offer Service")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startOfferForegroundService()
        handler.post {
            trackSec(10)
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun trackSec(time: Int) {
        for(i in time downTo 0){
            Thread.sleep(1000)
            // Notification Update....
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationBuilder.setContentText("$i seconds to redeem the offer!").setSilent(true)
            notificationManager.notify(111,notificationBuilder.build())
        }
    }

    fun startOfferForegroundService(){
        notificationBuilder = getNotificationBuilder()
        createNotificationChannel()
        startForeground(111,notificationBuilder.build())
    }

    fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
    }

    fun createNotificationChannel(): NotificationChannel {
        val channel = NotificationChannel("ChannelId","WM", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = ContextCompat.getSystemService(this,NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
        return channel
    }

    fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationBuilder = NotificationCompat.Builder(this,"ChannelId")
            .setContentTitle("Offer you can't refuse")
            .setContentText("60% off on selected items!")
            .setContentIntent(getPendingIntent())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
        return notificationBuilder
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerThread.quitSafely()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}