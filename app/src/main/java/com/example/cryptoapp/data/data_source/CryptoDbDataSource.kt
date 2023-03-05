package com.example.cryptoapp.data.data_source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.presentation.item.CryptoItem
import com.example.cryptoapp.presentation.main_screen.MainViewModel
//import com.example.cryptoapp.domain.service.CryptoRemoteDataSource
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 20

class CryptoDbDataSource(
    private val db: CryptoDatabase
) {

    fun getCryptos(): Flow<PagingData<CryptoItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CryptoDbPagingSource(db, true)
            }
        ).flow
    }

}