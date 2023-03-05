package com.example.cryptoapp.presentation.splash_screen

import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.dao.CryptoDao
import com.example.cryptoapp.domain.dto.toCryptoEntity
import com.example.cryptoapp.presentation.base.BaseViewModel
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.contracts.SplashContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val repository: SplashRepository,
) : BaseViewModel<SplashContract.Event, SplashContract.SplashState, SplashContract.Effect>() {

    override fun handleEvent(event: SplashContract.Event) {

    }

    override fun createInitialState(): SplashContract.SplashState {
        return SplashContract.SplashState.Default
    }

    // Fetching cryptos from server and inserting into DB
    fun insertCryptos() {
        viewModelScope.launch {

            val cryptos = repository.getCryptos()
            val entityList = cryptos.map { it.toCryptoEntity() }
            repository.insertCryptos(entityList)

            setEffect { SplashContract.Effect.OpenMainActivity }

        }
    }

}







