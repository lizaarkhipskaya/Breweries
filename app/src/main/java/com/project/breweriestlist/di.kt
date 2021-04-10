package com.project.breweriestlist

import com.project.breweriestlist.api.getBreweryApi
import com.project.breweriestlist.data.BreweriesRepository
import com.project.breweriestlist.data.buildDatabase
import com.project.breweriestlist.viewmodel.BreweriesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { getBreweryApi() }
    single { buildDatabase(androidContext()).breweryDao() }

    factory { BreweriesRepository(get(), get()) }

    viewModel { BreweriesViewModel(androidApplication(), get()) }
}