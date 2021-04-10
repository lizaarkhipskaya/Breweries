package com.project.breweriestlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreweryDao {
    @Query("SELECT * FROM brewery")
    fun getAllBreweries(): Flow<List<Brewery>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBreweries(list: List<Brewery>)

    @Query("SELECT * FROM brewery WHERE name LIKE :breweryName")
    suspend fun getBreweriesByName(breweryName: String): List<Brewery>
}