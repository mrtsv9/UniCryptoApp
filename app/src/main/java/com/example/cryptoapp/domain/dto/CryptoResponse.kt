package com.example.cryptoapp.domain.dto

import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.presentation.item.CryptoItem
import com.google.gson.annotations.SerializedName

data class CryptoResponse (
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

fun CryptoResponse.toCryptoItem(): CryptoItem {
    return CryptoItem(
        abbr = abbr,
        title = title,
        imageLink = imageLink,
        price = price
    )
}

fun CryptoResponse.toCryptoEntity(): CryptoEntity {
    return CryptoEntity(
        id = id,
        abbr = abbr,
        title = title,
        imageLink = imageLink,
        price = price
    )
}
