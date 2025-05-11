package com.example.workmanagerkotlin.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlin.concurrent.thread

/*** Services:-
 Top level components like Activities.
 To perform long running operations in background.
 It does not need any user-interface.
 Handle network transactions, play musics, perform file I/O - all from the background.
 */

/*** 🔹 Types of Services:
 Foreground Service
   Runs with a notification to keep it alive.
   Used for things like music playback, fitness tracking, etc.
   Example: Spotify playing music in background.

 Background Service
   Runs silently in the background (limited by Android ≥ Oreo).
   For less-visible tasks like syncing data.
   Requires permission and usage limits on Android 8.0+.

 Bound Service
   Offers a client-server interface that components can bind to.
   Allows components (like activities) to interact with the service.
   Stops when all clients unbind.

🔹 Life Cycle of a Service:
     onCreate(): Called once when the service is created.
     onStartCommand(): Called every time startService() is called.
     onBind(): Called when another component binds to the service using bindService().
     onDestroy(): Called when the service is no longer used.
 */

class BackgroundService: Service(){

    private val tag = "BackS"

    override fun onCreate() {
        Log.d(tag, "onCreate() is called")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand() is called")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy() is called")
        thread(start = true){
            while(true){
                Log.d(tag, "Work is in progress!!!!!!!!!")
                Thread.sleep(1000)
            }
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(tag, "onBind() is called")
        return null
    }

}

