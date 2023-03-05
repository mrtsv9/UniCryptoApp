package com.example.cryptoapp.presentation.main_screen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.entities.toCryptoItem
import com.example.cryptoapp.domain.dto.toCryptoEntity
import com.example.cryptoapp.presentation.base.BaseViewModel
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.item.CryptoItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainViewModel constructor(
    private val repository: MainRepository
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    override fun createInitialState(): MainContract.State {
        return MainContract.State(MainContract.CryptoState.Default)
    }

    override fun handleEvent(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.OnSortClicked -> {
                setEffect { MainContract.Effect.ShowDialogFragment }
            }
            is MainContract.Event.OnUpdateScroll -> {
                setEffect { MainContract.Effect.Update }
            }
        }
    }

//    private val fetchedCryptos = flow {
//        val response = repository.getCryptosFromServer()
//        if (response.isSuccessful) response.body()?.let { emit(it) }
//    }

    fun refreshCryptos() {
        viewModelScope.launch {
            val cryptos = repository.getCryptosFromServer()

            val entityList = cryptos.map { it.toCryptoEntity() }

            repository.insertCryptos(entityList)

            setEffect { MainContract.Effect.UpdateRecycler }

        }

//        viewModelScope.launch {
//            fetchedCryptos.collectLatest { list ->
//                val entityList = list.map {
//                    it.toCryptoEntity()
//                }
//                flow {
//                    emit(
//                        entityList.forEach {
//                            db.cryptoDao().insertCrypto(it)
//                        }
//                    )
//                }.collectLatest {
//                    setEffect { MainContract.Effect.UpdateRecycler }
//                }
//            }
//        }
    }

    fun getCryptosFromDb(db: CryptoDatabase) {
        viewModelScope.launch {
            flow {
                emit(
                    db.cryptoDao().getAllCrypto()
                )
            }.collect { list ->
                val itemList = list.forEach { it.toCryptoItem() }
            }
        }
    }

    fun getCryptosPagingData(): Flow<PagingData<CryptoItem>> {
        return repository.getCryptosPagingData().cachedIn(viewModelScope)
    }

    fun getCryptosAlphabeticallyPagingData(): Flow<PagingData<CryptoItem>> {
        return repository.getCryptosAlphabeticallyPagingData().cachedIn(viewModelScope)
    }

    fun getCryptosByPricePagingData(): Flow<PagingData<CryptoItem>> {
        return repository.getCryptosByPricePagingData().cachedIn(viewModelScope)
    }

    fun getCryptosFromDbPagingData(): Flow<PagingData<CryptoItem>> {
        return repository.getCryptosFromDbPagingData().cachedIn(viewModelScope)
    }

}