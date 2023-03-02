package com.example.cryptoapp.presentation.contracts

import com.example.cryptoapp.presentation.base.UiEffect
import com.example.cryptoapp.presentation.base.UiEvent
import com.example.cryptoapp.presentation.base.UiState

class MainContract {

    sealed class Event: UiEvent {
//        object OnInternetCheckFailure: Event()
//        object OnInternetCheckSuccess: Event()
    }

    data class State(
        val cryptoState: CryptoState
    ): UiState

    sealed class CryptoState {
//        object Loading: CryptoState()
        object Error: CryptoState() // Пока что ничего не делает
        object Success: CryptoState()
    }

    sealed class Effect: UiEffect {

//        object InternetError: Effect()
//        object InternetSuccess: Effect()

    }


}