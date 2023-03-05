package com.example.cryptoapp.presentation.contracts

import com.example.cryptoapp.presentation.base.UiEffect
import com.example.cryptoapp.presentation.base.UiEvent
import com.example.cryptoapp.presentation.base.UiState

class SplashContract {

    sealed class SplashState: UiState {
        object Default : SplashState()
    }

    sealed class Event : UiEvent

    sealed class Effect : UiEffect {
        object OpenMainActivity : Effect()
    }

}