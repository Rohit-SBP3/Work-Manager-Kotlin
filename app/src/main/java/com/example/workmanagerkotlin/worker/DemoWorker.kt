package com.example.workmanagerkotlin.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class DemoWorker(context: Context, params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result{
        performWork()
        // return Result.failure()
        // return Result.retry() it repeats, so we can add a policy
        //return Result.success()
        return Result.success()
    }

    private fun performWork(){
        Thread.sleep(3000)
        Log.d("Worker", "Task is Completed!")
    }

}