package com.example.cryptoapp.presentation.settings_screen

import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.data.entities.PersonEntity
import com.example.cryptoapp.data.entities.toPersonItem
import com.example.cryptoapp.presentation.item.PersonItem
import com.example.cryptoapp.presentation.item.toPersonEntity

class SettingsRepository(
    private val db: CryptoDatabase
) {

    suspend fun insertPerson(person: PersonItem) {
        db.cryptoDao().insertPerson(person.toPersonEntity())
    }

    suspend fun getPerson(): PersonEntity {
        return db.cryptoDao().getPerson()
    }

}