package com.abakan.electronics.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CountryEntity::class], version = 1)
internal abstract class CountriesDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}