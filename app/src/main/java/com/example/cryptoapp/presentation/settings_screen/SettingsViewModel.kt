package com.example.cryptoapp.presentation.settings_screen

import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.entities.PersonEntity
import com.example.cryptoapp.presentation.base.BaseViewModel
import com.example.cryptoapp.presentation.contracts.MainContract
import com.example.cryptoapp.presentation.contracts.SettingsContract
import com.example.cryptoapp.presentation.item.PersonItem
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

    fun insertPerson(person: PersonItem) {
        viewModelScope.launch {
            repository.insertPerson(person)
        }
    }

    fun getPerson(): PersonEntity? {

        var person: PersonEntity? = null
        viewModelScope.launch {
            runBlocking {
                person = repository.getPerson()
            }
        }
        return person
    }

}