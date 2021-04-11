package com.project.breweriestlist.api

import com.project.breweriestlist.data.Brewery
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL =
    "https://api.openbrewerydb.org/"

/*private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()*/

interface BreweryApiService {
    @GET("breweries")
    suspend fun searchBreweries(): List<Brewery>

    @GET("breweries")
    suspend fun searchBreweryByName(@Query("by_name") name: String): List<Brewery>
}

/*
object BreweryApi {
    val retrofitService: BreweryApiService by lazy {
        retrofit.create(BreweryApiService::class.java)
    }
}*/


fun getBreweryApi(): BreweryApiService {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    return retrofit.create(BreweryApiService::class.java)
}