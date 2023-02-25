package com.example.cryptoapp.presentation.contracts

import com.example.cryptoapp.data.db.entities.Crypto
//import com.example.cryptoapp.data.models.Crypto
//import com.example.cryptoapp.domain.dto.CryptoDto
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
//        data class Success(val cryptos: List<CryptoDto>?): CryptoState()
//        data class Success(val cryptos: List<Crypto>?): CryptoState()
        object Success: CryptoState()
    }

    sealed class Effect: UiEffect {

        object LoadingError: Effect() // Пока что ничего не делает

    }


}