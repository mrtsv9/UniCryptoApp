package com.example.cryptoapp.domain

import com.example.cryptoapp.domain.dao.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

//    private const val URL = "https://api.coingecko.com/api/v3/"
//
//    private fun getRetrofit(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

    companion object {
        private const val URL = "https://api.coingecko.com/api/v3/"

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}