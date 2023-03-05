package com.example.cryptoapp.presentation.main_screen

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.dao.CryptoDao
import com.example.cryptoapp.data.data_source.CryptoDbDataSource
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.data.entities.toCryptoItem
import com.example.cryptoapp.domain.RetrofitInstance
import com.example.cryptoapp.domain.data_source.CryptoRemoteDataSource
import com.example.cryptoapp.domain.service.ApiService
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.domain.dto.toCryptoItem
import com.example.cryptoapp.presentation.item.CryptoItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class MainRepository(
    private val remoteDataSource: CryptoRemoteDataSource,
    private val localDataSource: CryptoDbDataSource,
    private val cryptoDao: CryptoDao
) {

    suspend fun getCryptosFromServer(): List<CryptoResponse> {
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        retrofitInstance.getCryptos().body()?.let { return it }
        return emptyList()
    }

    suspend fun insertCryptos(cryptos: List<CryptoEntity>) {
        cryptoDao.insertAllCrypto(cryptos)
    }

    fun getCryptosPagingData(): Flow<PagingData<CryptoItem>> {
        return remoteDataSource.getCryptos()
            .map { pagingData ->
                pagingData.map {
                    it.toCryptoItem()
                }
            }
    }

    fun getCryptosAlphabeticallyPagingData(): Flow<PagingData<CryptoItem>> {
        return remoteDataSource.getCryptosAlphabetically()
            .map { pagingData ->
                pagingData.map {
                    it.toCryptoItem()
                }
            }
    }

    fun getCryptosByPricePagingData(): Flow<PagingData<CryptoItem>> {
        return remoteDataSource.getCryptosByPrice()
            .map { pagingData ->
                pagingData.map {
                    it.toCryptoItem()
                }
            }
    }

    fun getCryptosFromDbPagingData(): Flow<PagingData<CryptoItem>> {
        return localDataSource.getCryptos()
            .map { pagingData ->
                pagingData.map { it }
            }
    }


}