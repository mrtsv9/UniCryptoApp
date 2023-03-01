package com.example.cryptoapp.domain.data_source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cryptoapp.domain.dto.CryptoResponse
//import com.example.cryptoapp.domain.service.CryptoRemoteDataSource
import kotlinx.coroutines.flow.Flow

const val NETWORK_PAGE_SIZE = 20

class CryptoRemoteDataSource {

    fun getCryptos(): Flow<PagingData<CryptoResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CryptoRemotePagingSource()
            }
        ).flow
    }

}