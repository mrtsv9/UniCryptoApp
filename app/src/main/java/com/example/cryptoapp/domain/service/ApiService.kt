package com.example.cryptoapp.domain.service

import com.example.cryptoapp.domain.dto.CryptoDetailsResponse
import com.example.cryptoapp.domain.dto.CryptoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false")
    suspend fun getCryptos(): Response<List<CryptoResponse>>

    @GET("coins/markets?vs_currency=usd&per_page=20")
    suspend fun getCryptosByPage(
        @Query("page") page: String,
        @Query("order") alphabetically: String? = null,
        @Query("order") price: String? = null
    ): Response<List<CryptoResponse>>

    @GET("coins/{id}/market_chart?vs_currency=usd")
    suspend fun getDetailCrypto(
        @Path("id") id: String,
        @Query("days") days: String
    ): Response<CryptoDetailsResponse>

}