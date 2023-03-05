package com.example.cryptoapp.presentation.contracts

import com.example.cryptoapp.presentation.base.UiEffect
import com.example.cryptoapp.presentation.base.UiEvent
import com.example.cryptoapp.presentation.base.UiState

class SettingsContract {

    sealed class Event : UiEvent

    data class State(
        val settingsCryptoState: SettingsCryptoState
    ) : UiState

    sealed class SettingsCryptoState {
        object Empty : SettingsCryptoState()
    }

    sealed class Effect : UiEffect
}