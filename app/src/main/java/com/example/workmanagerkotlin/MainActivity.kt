package com.example.workmanagerkotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.work.BackoffPolicy
import androidx.work.Constraints.Builder
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.workmanagerkotlin.ui.theme.WorkManagerKotlinTheme
import com.example.workmanagerkotlin.worker.DemoWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val workManager = WorkManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkManagerKotlinTheme {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text("Hello Work Manager!!")
                }
            }
        }

        doPeriodicWork()
    }

    private fun doWork(){
        val request = OneTimeWorkRequest.Builder(DemoWorker::class.java)
            .setConstraints(Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                10,
                TimeUnit.SECONDS
            )
            .build()

        val request2 = OneTimeWorkRequest.Builder(DemoWorker::class.java)
            .setConstraints(Builder().setRequiresCharging(true).build())
            .build()

        workManager.beginWith(request).then(request2).enqueue()
        //workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id).observe(this){
            if(it != null ) printStatus(it.state.name)
        }
    }

    private fun doPeriodicWork(){
        val request = PeriodicWorkRequest.Builder(DemoWorker::class.java,15,TimeUnit.MINUTES)
            .setConstraints(Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                10,
                TimeUnit.SECONDS
            )
            .build()

        workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id).observe(this){
            if(it != null ) printStatus(it.state.name)
        }
    }

    private fun printStatus(name: String) {
        Log.d("Main Worker", name)
    }
}

/*** Work Manager:-
 * WorkManager is an Android Jetpack library used to schedule and run deferrable, guaranteed background work—even if the app exits or the device restarts.
 * Deferrable, guaranteed  background work when the work's constraints are satisfied.
 * Best practice in android for long running tasks.
 *
 * 🔧 Key Features:
 * Guaranteed Execution: Work will be run eventually, even if the app is killed.
 * Battery-efficient: Respects Doze Mode and App Standby.
 * Constraints Support: Like network availability, charging status, storage space, etc.
 * Backoff & Retry Mechanism: Automatically retries failed work with exponential backoff.
 *
 * 🔍 Types of Work:
 * OneTimeWorkRequest – run once
 * PeriodicWorkRequest – repeat work periodically
 */

