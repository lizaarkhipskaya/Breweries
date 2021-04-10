package com.project.breweriestlist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_NAME)
data class Brewery(
    @PrimaryKey val id: Int,
    val name: String,
    val street: String?,
    val city: String,
    val state: String,
    val country: String,
    val longitude: Double,
    val latitude: Double,
    val phone: String?,
    @ColumnInfo(name = "website_url")
    @SerializedName("website_url")
    val websiteUrl: String?
) {
    fun areCoordinatesValid() = longitude != 0.0 && latitude != 0.0
}