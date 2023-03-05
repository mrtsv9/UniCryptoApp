package com.example.cryptoapp.presentation.main_screen

import com.example.cryptoapp.domain.dto.CryptoResponse
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.random.Random

class FakeMainRepository {

    private val cryptos = mutableListOf<CryptoResponse>()

    @Test
    fun `check if price equals value`() {
        getCryptos()
        assertThat(getTotalPrice()).isEqualTo(100.0)
    }

    @Test
    fun `check if id's equals to value`() {
        getCryptos()
        assertThat(getCryptosId()).isEqualTo(listOf("bitcoin", "usdt", "eth"))
    }

    private fun getCryptos() {
        cryptos.clear()
        cryptos.addAll(
            listOf(
            CryptoResponse(
                "bitcoin",
                "btc",
                "title",
                "link",
                "50",
                "100",
                "0"
            ),
            CryptoResponse(
                "usdt",
                "tether",
                "title",
                "link",
                "30",
                "100",
                "0"
            ),
                CryptoResponse(
                "eth",
                "ethereum",
                "title",
                "link",
                "20",
                "100",
                "0"
            )))
    }

    private fun getTotalPrice(): Double {
        return cryptos.sumOf { it.price.toDouble() }
    }

    private fun getCryptosId(): List<String> {
        getCryptos()
        val idList = mutableListOf<String>()
        cryptos.forEach {
            idList.add(it.id)
        }
        return idList
    }

}