package com.example.cryptoapp.presentation.details_screen

import com.example.cryptoapp.domain.RetrofitInstance
import com.example.cryptoapp.domain.dto.CryptoDetailsResponse
import com.example.cryptoapp.domain.service.ApiService
import retrofit2.Response

class DetailsRepository {

    suspend fun getCryptoDetail(id: String, days: String): CryptoDetailsResponse? {
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        return retrofitInstance.getDetailCrypto(id = id, days = days).body()
    }

}