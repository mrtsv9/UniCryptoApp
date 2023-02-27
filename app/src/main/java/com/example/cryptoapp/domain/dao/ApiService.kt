package com.example.cryptoapp.domain.dao

import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.domain.dto.CryptoResponse
//import com.example.cryptoapp.domain.dto.CryptoDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("coins/markets?vs_currency=usd&per_page=20")
//    suspend fun getCryptos(): Response<List<CryptoDto>>
//    suspend fun getCryptos(): Response<List<CryptoEntity>>
//    suspend fun getCryptos(): Response<List<CryptoResponse>>
    suspend fun getCryptos(): Response<List<CryptoResponse>>

    @GET()
//    suspend fun getSingleCrypto(): Response<CryptoDto>
//    suspend fun getSingleCrypto(): Response<CryptoEntity>
    suspend fun getSingleCrypto(): Response<CryptoResponse>

}