package com.example.cryptoapp.domain.dto

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