package com.example.cryptoapp.domain.service

import com.example.cryptoapp.domain.dto.CryptoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("coins/markets?vs_currency=usd&per_page=20")
    suspend fun getCryptos(): Response<List<CryptoResponse>>

    @GET("coins/markets?vs_currency=usd&per_page=20")
    suspend fun getCryptosByPage(@Query("page") page: String): Response<List<CryptoResponse>>

    @GET()
    suspend fun getSingleCrypto(): Response<CryptoResponse>

}