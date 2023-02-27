package com.example.cryptoapp.presentation.main_screen

import com.example.cryptoapp.domain.RetrofitInstance
import com.example.cryptoapp.domain.dao.ApiService
import com.example.cryptoapp.domain.dto.CryptoResponse
//import com.example.cryptoapp.domain.dto.CryptoDto
import retrofit2.Response

class MainRepository() {

    suspend fun getCryptos(): Response<List<CryptoResponse>> {
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        return retrofitInstance.getCryptos()
    }

}