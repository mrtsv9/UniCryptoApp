package com.example.cryptoapp.presentation.item

import java.io.Serializable

data class CryptoItem(
    val id: String?,
    val abbr: String?,
    val title: String?,
    val imageLink: String?,
    val price: String?,
    var marketCap: String?,
    var priceChange: String?
) : Serializable