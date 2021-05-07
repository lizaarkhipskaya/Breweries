package com.project.breweriestlist.data

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.project.breweriestlist.api.BreweryApiService
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

const val LOG_TAG = "BreweriesRepository"

@Singleton
class BreweriesRepository @Inject constructor(
    val databaseDao: BreweryDao,
    val breweryApiService: BreweryApiService
) {

    val list: LiveData<List<Brewery>> = databaseDao.getAllBreweries().asLiveData()

    private val _searchByDb = MutableLiveData<List<Brewery>>()
    private val _searchByApi = MutableLiveData<List<Brewery>>()

    val searchList by lazy {
        MediatorLiveData<List<Brewery>>().apply{
            addSource(_searchByApi) {
                value = it
            }
            addSource(_searchByDb) {
                value = it
            }
        }
    }

    suspend fun refreshBreweries() {
        withContext(Dispatchers.IO) {
            Log.d(LOG_TAG, "refreshBreweries()")
            val list = breweryApiService.searchBreweries()
            databaseDao.insertAllBreweries(list)
        }
    }

    suspend fun getBreweriesBySearchedName(searchedName: String, appContext: Context) {
        Log.d(LOG_TAG, "getBreweriesBySearchedName: $searchedName, $appContext")
        withContext(Dispatchers.IO) {
            getSearchListDependingOnNetworkState(searchedName, appContext)
        }
    }

    private suspend fun getSearchListDependingOnNetworkState(input: String, context: Context) {
        if (isInternetAvailable(context)) {
            Log.d(LOG_TAG, "search brewery by Retrofit: $input, $context")
            _searchByApi.postValue(breweryApiService.searchBreweryByName(input))
        } else {
            Log.d(LOG_TAG, "search brewery by database: $input, $context")
            _searchByDb.postValue(databaseDao.getBreweriesByName("%$input%"))
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo //todo
        return networkInfo != null && networkInfo.isConnected
    }
}