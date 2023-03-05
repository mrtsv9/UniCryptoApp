package com.example.cryptoapp.presentation.details_screen

import com.example.cryptoapp.domain.dto.CryptoDetailsResponse
import com.example.cryptoapp.presentation.base.BaseViewModel
import com.example.cryptoapp.presentation.contracts.DetailsContract
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class DetailsViewModel(
    private val repository: DetailsRepository
) : BaseViewModel<DetailsContract.Event, DetailsContract.State, DetailsContract.Effect>() {

    override fun createInitialState(): DetailsContract.State {
        return DetailsContract.State(DetailsContract.DetailsCryptoState.Success)
    }

    override fun handleEvent(event: DetailsContract.Event) {
        when (event) {

        }
    }

    suspend fun getDetailCrypto(id: String, days: String): CryptoDetailsResponse? {
        return repository.getCryptoDetail(id, days)
    }

}