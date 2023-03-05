package com.example.cryptoapp.presentation.main_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.MainCoroutineRule
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.dao.CryptoDao
import com.example.cryptoapp.data.data_source.CryptoDbDataSource
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.data.entities.toCryptoItem
import com.example.cryptoapp.data.entities.toCryptoResponse
import com.example.cryptoapp.domain.data_source.CryptoRemoteDataSource
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.domain.dto.toCryptoEntity
import com.example.cryptoapp.domain.dto.toCryptoItem
import com.example.cryptoapp.presentation.contracts.MainContract
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var db: CryptoDatabase
    private lateinit var dao: CryptoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CryptoDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.cryptoDao()
        viewModel = MainViewModel(MainRepository(CryptoRemoteDataSource(), CryptoDbDataSource(db), dao))
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testingCryptosInsert() = runBlockingTest {
        val response = fetchCryptos()

        response.forEach {
            dao.insertCrypto(it.toCryptoEntity())
        }
        assertThat(dao.getAllCrypto()).isNotEqualTo(response)
        val items = mutableListOf<CryptoEntity>()
        response.forEach {
            items.add(it.toCryptoEntity())
        }
        assertThat(dao.getAllCrypto()).isEqualTo(items)
    }

    @Test
    fun testingDefaultUiState() {
        assertThat(viewModel.uiState.value.cryptoState).isEqualTo(MainContract.CryptoState.Default)
    }

    @Test
    fun testingOrderedAlphabeticallyUiState() {
        fetchCryptosAlphabeticallyByPage()
        assertThat(viewModel.uiState.value.cryptoState)
            .isEqualTo(MainContract.CryptoState.OrderedAlphabetically)
    }

    private fun fetchCryptosAlphabeticallyByPage() {
        // simulate fetching data
        viewModel.setState { copy(MainContract.CryptoState.OrderedAlphabetically) }
    }

    @Test
    fun testingOrderedByPriceUiState() {
        fetchCryptosByPriceByPage()
        assertThat(viewModel.uiState.value.cryptoState)
            .isEqualTo(MainContract.CryptoState.OrderedByPrice)
    }

    private fun fetchCryptosByPriceByPage() {
        // simulate fetching data
        viewModel.setState { copy(MainContract.CryptoState.OrderedByPrice) }
    }

    private fun fetchCryptos(): List<CryptoResponse> {
        return listOf(
            CryptoResponse(
                "bitcoin",
                "btc",
                "title",
                "link",
                "50",
                "100",
                "0"
            ),
            CryptoResponse(
                "usdt",
                "tether",
                "title",
                "link",
                "30",
                "100",
                "0"
            ),
            CryptoResponse(
                "eth",
                "ethereum",
                "title",
                "link",
                "20",
                "100",
                "0"
            ))
    }

}