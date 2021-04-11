package com.project.breweriestlist.data

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.project.breweriestlist.api.BreweryApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val LOG_TAG = "BreweriesRepository"

class BreweriesRepository @Inject constructor(
    val databaseDao: BreweryDao,
    val breweryApiService: BreweryApiService
) {

    val list: LiveData<List<Brewery>> = databaseDao.getAllBreweries().asLiveData()

    private var _searchList = MutableLiveData<List<Brewery>>()
    val searchList: LiveData<List<Brewery>>
        get() = _searchList

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
            val list = getSearchListDependingOnNetworkState(searchedName, appContext)
            withContext(Dispatchers.Main) {
                _searchList.value = list
            }
        }
    }

    private suspend fun getSearchListDependingOnNetworkState(input: String, context: Context) =
        if (isInternetAvailable(context)) {
            Log.d(LOG_TAG, "search brewery by Retrofit: $input, $context")
            breweryApiService.searchBreweryByName(input)
        } else {
            Log.d(LOG_TAG, "search brewery by database: $input, $context")
            databaseDao.getBreweriesByName("%$input%")
        }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo //todo
        return networkInfo != null && networkInfo.isConnected
    }
}