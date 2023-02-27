package com.example.cryptoapp.data.dao

import androidx.room.*
import com.example.cryptoapp.data.entities.CryptoEntity

@Dao
interface CryptoDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert()
    suspend fun insertCrypto(crypto: CryptoEntity)

    @Query("SELECT * FROM crypto_table")
    suspend fun getAllCrypto(): List<CryptoEntity>

}