package com.project.breweriestlist.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.breweriestlist.data.BreweriesRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException

private const val LOG_TAG = "RefreshBreweryDataWorker"

class RefreshBreweryDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val repository: BreweriesRepository by lazy {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(appContext,
        RefreshBreweryDataWorkerEntryPoint::class.java)
        hiltEntryPoint.repository()
    }

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

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface RefreshBreweryDataWorkerEntryPoint {
        fun repository(): BreweriesRepository
    }
}