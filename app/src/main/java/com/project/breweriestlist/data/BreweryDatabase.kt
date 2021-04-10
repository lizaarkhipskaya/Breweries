package com.project.breweriestlist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val TABLE_NAME = "brewery"

@Database(entities = [Brewery::class], version = 1, exportSchema = false)
abstract class BreweryDatabase : RoomDatabase() {
    abstract fun breweryDao(): BreweryDao

/*    companion object {
        @Volatile
        private var INSTANCE: BreweryDatabase? = null

        fun getInstance(context: Context): BreweryDatabase {
            synchronized(this) {
                return INSTANCE
                    ?: buildDatabase(
                        context
                    ).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): BreweryDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BreweryDatabase::class.java,
                TABLE_NAME
            ).fallbackToDestructiveMigration()
                .build()
        }
    }*/
}


fun buildDatabase(context: Context): BreweryDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        BreweryDatabase::class.java,
        TABLE_NAME
    ).fallbackToDestructiveMigration()
        .build()
}
