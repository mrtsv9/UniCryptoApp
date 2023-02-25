package com.example.cryptoapp.presentation.main_screen

import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.db.CryptoDatabase
import com.example.cryptoapp.data.db.entities.Crypto
import com.example.cryptoapp.presentation.base.BaseViewModel
import com.example.cryptoapp.presentation.contracts.MainContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                withContext(Dispatchers.Default) {
                    val response = repository.getCryptos()
                    if (response.isSuccessful) {
                        val cryptos = response.body()
                        cryptos?.forEach {
                            db.cryptoDao().insertCrypto(it)
                        }
                        setState { copy(cryptoState = MainContract.CryptoState.Success) }
                    }
                }
            }
            catch (e: Exception) {
                setEffect { MainContract.Effect.LoadingError } // Пока что ничего не делает
            }
        }
    }

    suspend fun getCryptosFromDb(db: CryptoDatabase): List<Crypto> {
        val cryptos = emptyList<Crypto>().toMutableList()
        cryptos.addAll( db.cryptoDao().getAllCrypto())
        return cryptos
    }

}