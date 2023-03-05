package com.example.cryptoapp.domain

import com.TestCoroutineRule
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.domain.service.ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import retrofit2.Response

@ExperimentalCoroutinesApi
class ApiServiceTest {

    @get:Rule
    val mainCoroutineRule = TestCoroutineRule()

    @Test
    fun `check if the result http code is 200`() = runBlockingTest {
        val retrofitInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

        val response = runBlocking {
            retrofitInstance.getCryptos()
        }

        assertThat(response.code()).isEqualTo(200)

    }

}