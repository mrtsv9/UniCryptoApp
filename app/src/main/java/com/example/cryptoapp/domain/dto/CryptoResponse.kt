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
    var price: String,
    @SerializedName("market_cap")
    var marketCap: String?,
    @SerializedName("price_change_percentage_24h")
    var priceChange: String?

)

fun CryptoResponse.toCryptoItem(): CryptoItem {
    return CryptoItem(
        id = id,
        abbr = abbr,
        title = title,
        imageLink = imageLink,
        price = price,
        marketCap = marketCap,
        priceChange = priceChange
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
