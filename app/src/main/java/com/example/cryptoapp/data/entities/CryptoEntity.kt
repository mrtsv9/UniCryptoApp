package com.example.cryptoapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto_table")
data class CryptoEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var abbr: String,
    var title: String,
    var imageLink: String,
    var price: String
)
