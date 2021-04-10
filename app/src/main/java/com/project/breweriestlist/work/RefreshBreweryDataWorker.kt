package com.project.breweriestlist.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.breweriestlist.data.BreweriesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException

private const val LOG_TAG = "RefreshBreweryDataWorker"
class RefreshBreweryDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params), KoinComponent {

    private val repository : BreweriesRepository by inject()

    override suspend fun doWork(): Result {
        try {
            Log.i(LOG_TAG, "doWork(): try to refresh breweries")
            repository.refreshBreweries()
        } catch (e: HttpException) {
            Log.e(LOG_TAG, "doWork(): ${e.message()}")
            return Result.retry()
        }
        return Result.success()
    }
}