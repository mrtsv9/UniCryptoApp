package com.example.cryptoapp.data.db.entities

import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import com.google.android.material.circularreveal.CircularRevealHelper
import com.google.gson.annotations.SerializedName

@Entity(tableName = "crypto_table")
data class Crypto(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    @SerializedName("symbol")
    var abbr: String,
    @SerializedName("name")
    var title: String,
    @SerializedName("image")
    var imageLink: String,
    @SerializedName("current_price")
    var price: String
)
