package com.example.cryptoapp.presentation.contracts

import com.example.cryptoapp.presentation.base.UiEffect
import com.example.cryptoapp.presentation.base.UiEvent
import com.example.cryptoapp.presentation.base.UiState

class MainContract {

    sealed class Event: UiEvent {
//        object OnRandomNumberClicked: Event()
//        object OnShowToastClicked: Event()
    }

    data class State(
        val cryptoState: CryptoState
    ): UiState

    sealed class CryptoState {
        object Loading: CryptoState() // Пока что ничего не делает
        object Error: CryptoState() // Пока что ничего не делает
        object Success: CryptoState()
    }

    sealed class Effect: UiEffect {

        object LoadingError: Effect() // Пока что ничего не делает

    }


}