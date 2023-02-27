package com.example.cryptoapp.presentation.main_screen

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.presentation.base.BaseViewModel
import com.example.cryptoapp.presentation.contracts.MainContract
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainViewModel(): BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    private val repository = MainRepository()

    override fun createInitialState(): MainContract.State {
        return MainContract.State(MainContract.CryptoState.Success)
    }

    override fun handleEvent(event: MainContract.Event) {

    }

    // Fetching cryptos from server and inserting in DB
    fun insertCryptos(db: CryptoDatabase) {
        viewModelScope.launch {
            try {
//                Log.d("KEK", "Getting cryptos..")
                val cryptos = runBlocking { fetchCryptos() }
//                Log.d("KEK", "Fetched data from API")
                runBlocking {
                    cryptos?.forEach {
                        db.cryptoDao().insertCrypto(
                            CryptoEntity(it.id, it.abbr, it.title, it.imageLink, it.price)
                        )
                    }
                }
//                    Log.d("KEK", "Inserted into DB")
                    setState { copy(cryptoState = MainContract.CryptoState.Success) }
            }
            catch (e: Exception) {
                setEffect { MainContract.Effect.LoadingError } // Пока что ничего не делает
            }
        }
    }

    private suspend fun fetchCryptos(): List<CryptoResponse>? {
        val response = repository.getCryptos()
        return if (response.isSuccessful) response.body()
        else emptyList()
    }

    suspend fun getCryptosFromDb(db: CryptoDatabase): List<CryptoEntity> {
        val cryptos = emptyList<CryptoEntity>().toMutableList()
        cryptos.addAll(db.cryptoDao().getAllCrypto())
        Log.d("KEK", "Got from DB")
        return cryptos
    }

}