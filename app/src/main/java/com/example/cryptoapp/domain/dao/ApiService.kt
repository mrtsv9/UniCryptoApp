package com.example.cryptoapp.domain.dao

import com.example.cryptoapp.data.db.entities.Crypto
//import com.example.cryptoapp.domain.dto.CryptoDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("coins/markets?vs_currency=usd&per_page=20")
//    suspend fun getCryptos(): Response<List<CryptoDto>>
    suspend fun getCryptos(): Response<List<Crypto>>

    @GET()
//    suspend fun getSingleCrypto(): Response<CryptoDto>
    suspend fun getSingleCrypto(): Response<Crypto>

}