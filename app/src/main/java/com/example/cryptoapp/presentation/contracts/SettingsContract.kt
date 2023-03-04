package com.example.cryptoapp.presentation.contracts

import com.example.cryptoapp.presentation.base.UiEffect
import com.example.cryptoapp.presentation.base.UiEvent
import com.example.cryptoapp.presentation.base.UiState

class SettingsContract {

    sealed class Event: UiEvent {
//        object OnInternetCheckFailure: Event()
//        object OnInternetCheckSuccess: Event()
    }

    data class State(
        val settingsCryptoState: SettingsCryptoState
    ): UiState

    sealed class SettingsCryptoState {
        object Empty: SettingsCryptoState()
        object Loaded: SettingsCryptoState()
    }

    sealed class Effect: UiEffect {

//        object InternetError: Effect()
//        object InternetSuccess: Effect()

    }


}