package com.example.cryptoapp.domain.dto

import com.google.gson.annotations.SerializedName

data class CryptoDetailsResponse(
    val cryptoPrice: CryptoPrice
)

data class CryptoPrice(
    val price: Float
)