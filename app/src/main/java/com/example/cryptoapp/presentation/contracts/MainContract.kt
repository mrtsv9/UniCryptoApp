package com.example.cryptoapp.presentation.contracts

import com.example.cryptoapp.presentation.base.UiEffect
import com.example.cryptoapp.presentation.base.UiEvent
import com.example.cryptoapp.presentation.base.UiState

class MainContract {

    sealed class Event: UiEvent {
        object OnSortClicked: Event()
        object OnUpdateScroll: Event()
    }

    data class State(
        val cryptoState: CryptoState
    ): UiState

    sealed class CryptoState {
        object Default: CryptoState()
        object OrderedAlphabetically: CryptoState()
        object OrderedByPrice: CryptoState()
    }

    sealed class Effect: UiEffect {

        object ShowDialogFragment: Effect()
        object UpdateRecycler: Effect()

    }


}