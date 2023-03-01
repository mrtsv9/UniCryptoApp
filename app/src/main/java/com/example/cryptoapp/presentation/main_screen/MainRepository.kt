package com.example.cryptoapp.presentation.main_screen

import androidx.paging.PagingData
import androidx.paging.map
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.data_source.CryptoDbDataSource
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.data.entities.toCryptoItem
import com.example.cryptoapp.domain.RetrofitInstance
import com.example.cryptoapp.domain.data_source.CryptoRemoteDataSource
import com.example.cryptoapp.domain.service.ApiService
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.domain.dto.toCryptoItem
import com.example.cryptoapp.presentation.item.CryptoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class MainRepository(
    private val remoteDataSource: CryptoRemoteDataSource,
    private val localDataSource: CryptoDbDataSource,
    private val db: CryptoDatabase
) {

    suspend fun getCryptos(): Response<List<CryptoResponse>> {
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        return retrofitInstance.getCryptos()
    }

    suspend fun getCryptosFromDb(): List<CryptoEntity> {
        return db.cryptoDao().getAllCrypto()
    }

    fun getCryptosByPage(): Flow<PagingData<CryptoItem>> {
        return remoteDataSource.getCryptos()
            .map { pagingData ->
                pagingData.map {
                    it.toCryptoItem()
                }
            }
    }

    fun getCryptosByPageFromDb(): Flow<PagingData<CryptoItem>> {
        return localDataSource.getCryptos()
            .map { pagingData ->
                pagingData.map {
                    it.toCryptoItem()
                }
            }
    }


}