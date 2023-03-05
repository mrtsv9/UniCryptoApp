package com.example.cryptoapp.presentation.splash_screen

import com.example.cryptoapp.data.dao.CryptoDao
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.domain.RetrofitInstance
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.domain.service.ApiService
import retrofit2.Response

class SplashRepository(
    private val cryptoDao: CryptoDao
) {

    suspend fun getCryptos(): List<CryptoResponse> {
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        retrofitInstance.getCryptos().body()?.let { return it }
        return emptyList()
    }

    suspend fun insertCryptos(cryptos: List<CryptoEntity>) {
        cryptoDao.insertAllCrypto(cryptos)
    }

}
