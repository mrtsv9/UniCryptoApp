package com.example.cryptoapp.presentation.contracts

import com.example.cryptoapp.presentation.base.UiEffect
import com.example.cryptoapp.presentation.base.UiEvent
import com.example.cryptoapp.presentation.base.UiState

class DetailsContract {

    sealed class Event: UiEvent {
//        object OnInternetCheckFailure: Event()
//        object OnInternetCheckSuccess: Event()
    }

    data class State(
        val detailsCryptoState: DetailsCryptoState
    ): UiState

    sealed class DetailsCryptoState {
        object Loading: DetailsCryptoState()
        object Success: DetailsCryptoState()
    }

    sealed class Effect: UiEffect {

//        object InternetError: Effect()
//        object InternetSuccess: Effect()

    }


}