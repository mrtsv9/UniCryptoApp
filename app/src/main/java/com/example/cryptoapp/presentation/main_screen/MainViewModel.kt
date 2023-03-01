package com.example.cryptoapp.presentation.main_screen

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.domain.dto.toCryptoEntity
import com.example.cryptoapp.presentation.base.BaseViewModel
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.item.CryptoItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MainViewModel constructor(
    private val repository: MainRepository
): BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {


    override fun createInitialState(): MainContract.State {
        return MainContract.State(MainContract.CryptoState.Success)
    }

    override fun handleEvent(event: MainContract.Event) {

    }

    // Fetching cryptos from server and inserting in DB
    fun insertCryptos(db: CryptoDatabase) {
        viewModelScope.launch {
            try {
                val cryptos = runBlocking { fetchCryptos() }
                runBlocking {
                    cryptos?.forEach {
                        db.cryptoDao().insertCrypto(it.toCryptoEntity())
//                        db.cryptoDao().insertCrypto(
//                            CryptoEntity(it.id, it.abbr, it.title, it.imageLink, it.price)
//                        )
                    }
                }
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

    fun fetchCryptosByPage(): Flow<PagingData<CryptoItem>> {
        return repository.getCryptosByPage().cachedIn(viewModelScope)
    }

    fun fetchCryptosByPageFromDb(): Flow<PagingData<CryptoItem>> {
        return repository.getCryptosByPageFromDb().cachedIn(viewModelScope)
    }

    fun getCryptosFromDb(): List<CryptoEntity> {
        val list = mutableListOf<CryptoEntity>()
        viewModelScope.launch {
            runBlocking {
                list.addAll(repository.getCryptosFromDb())
            }
        }
        return list
    }

}