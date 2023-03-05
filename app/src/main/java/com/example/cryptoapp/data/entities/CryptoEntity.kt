package com.example.cryptoapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.presentation.item.CryptoItem

@Entity(tableName = "crypto_table")
data class CryptoEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var abbr: String,
    var title: String,
    var imageLink: String,
    var price: String
)

fun CryptoEntity.toCryptoItem(): CryptoItem{
    return CryptoItem(
        id = id,
        abbr = abbr,
        title = title,
        imageLink = imageLink,
        price = price,
        marketCap = null,
        priceChange = null
    )
}

fun CryptoEntity.toCryptoResponse(): CryptoResponse {
    return CryptoResponse(
        id = id,
        abbr = abbr,
        title = title,
        imageLink = imageLink,
        price = price,
        marketCap = null,
        priceChange = null
    )
}