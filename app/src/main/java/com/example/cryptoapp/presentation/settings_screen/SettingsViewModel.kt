package com.example.cryptoapp.presentation.settings_screen

import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.presentation.base.BaseViewModel
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.contracts.SettingsContract
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(
    private val repository: SettingsRepository
): BaseViewModel<SettingsContract.Event, SettingsContract.State,
        SettingsContract.Effect>() {

    override fun createInitialState(): SettingsContract.State {
        return SettingsContract.State(SettingsContract.SettingsCryptoState.Empty)
    }

    override fun handleEvent(event: SettingsContract.Event) {

    }



}