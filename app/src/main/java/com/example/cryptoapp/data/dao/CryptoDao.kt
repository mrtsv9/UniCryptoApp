package com.example.cryptoapp.data.dao

import androidx.paging.DataSource
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.presentation.item.CryptoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrypto(crypto: CryptoEntity)

    @Query("SELECT * FROM crypto_table")
    suspend fun getAllCrypto(): List<CryptoEntity>

}