package com.example.cryptoapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.MainCoroutineRule
import com.example.cryptoapp.data.dao.CryptoDao
import com.example.cryptoapp.data.data_source.CryptoDbDataSource
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.domain.data_source.CryptoRemoteDataSource
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.presentation.main_screen.MainRepository
import com.example.cryptoapp.presentation.main_screen.MainViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.concurrent.fixedRateTimer

class RoomDbTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var db: CryptoDatabase
    private lateinit var dao: CryptoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CryptoDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.cryptoDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testingInsertingCryptosMultipleTimes() = runBlockingTest {
        getCryptos().forEach {
            dao.insertCrypto(it)
        }
        val firstQuantity = dao.getAllCrypto().size
        getCryptos().forEach {
            dao.insertCrypto(it)
        }
        val secondQuantity = dao.getAllCrypto().size
        assertThat(secondQuantity).isEqualTo(firstQuantity)
    }

    private fun getCryptos(): List<CryptoEntity> {
        return listOf(
            CryptoEntity(
                "bitcoin",
                "btc",
                "title",
                "link",
                "50"
            ),
            CryptoEntity(
                "usdt",
                "tether",
                "title",
                "link",
                "30"
            ),
            CryptoEntity(
                "eth",
                "ethereum",
                "title",
                "link",
                "20"
            )
        )
    }


}