package com.project.breweriestlist

import android.app.Application
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.project.breweriestlist.work.RefreshBreweryDataWorker
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

private const val LOG_TAG = "BreweriesApplication"

@HiltAndroidApp
class BreweriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(LOG_TAG, "onCreate()")
        setUpWorkManger()
    }

    private fun setUpWorkManger() {
        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request =
            OneTimeWorkRequest.Builder(RefreshBreweryDataWorker::class.java)
                .setConstraints(constrains)
                .build()

        WorkManager.getInstance().beginWith(request).enqueue()
    }
}