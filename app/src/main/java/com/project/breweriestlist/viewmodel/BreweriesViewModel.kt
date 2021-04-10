package com.project.breweriestlist.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.breweriestlist.data.BreweriesRepository
import com.project.breweriestlist.data.LOG_TAG
import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "BreweriesViewModel"

class BreweriesViewModel(
    application: Application,
    private val repository: BreweriesRepository
) : AndroidViewModel(application) {

    val breweryList = repository.list
    val searchList = repository.searchList

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                repository.refreshBreweries()
            } catch (networkError: IOException) {
                Log.e(LOG_TAG, "refreshDataFromRepository(): ${networkError.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared(): ")
    }

    fun makeSearch(input: String, context: Context) {
        viewModelScope.launch {
            repository.getBreweriesBySearchedName(input, context)
        }
    }
}