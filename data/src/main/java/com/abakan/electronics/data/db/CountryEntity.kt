package com.abakan.electronics.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abakan.electronics.data.Country

@Entity(tableName = "countries")
internal data class CountryEntity(
    @PrimaryKey(autoGenerate = false) val uid: Int,
    val name: String,
    val capital: String,
    @ColumnInfo(name = "coat_of_arms_url") val coatOfArmsUrl: String
)

internal fun CountryEntity.asExternal(): Country = Country(name, capital, coatOfArmsUrl)


