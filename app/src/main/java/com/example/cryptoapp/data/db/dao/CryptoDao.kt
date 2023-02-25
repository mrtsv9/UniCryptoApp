package com.example.cryptoapp.data.db.dao

import androidx.room.*
import com.example.cryptoapp.data.db.entities.Crypto

@Dao
interface CryptoDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert()
    suspend fun insertCrypto(crypto: Crypto)

    @Query("SELECT * FROM crypto_table")
    suspend fun getAllCrypto(): List<Crypto>

}