package com.project.breweriestlist.di

import android.content.Context
import androidx.room.Room
import com.project.breweriestlist.data.BreweryDatabase
import com.project.breweriestlist.data.TABLE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideBreweryDao(database: BreweryDatabase) = database.breweryDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext.applicationContext,
            BreweryDatabase::class.java,
            TABLE_NAME
        ).fallbackToDestructiveMigration()
            .build()

}