package com.example.cryptoapp.di

import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.data_source.CryptoDbDataSource
import com.example.cryptoapp.data.data_source.CryptoDbPagingSource
import com.example.cryptoapp.domain.data_source.CryptoRemotePagingSource
import com.example.cryptoapp.domain.data_source.CryptoRemoteDataSource
import com.example.cryptoapp.domain.service.ApiService
import com.example.cryptoapp.presentation.details_screen.DetailsRepository
import com.example.cryptoapp.presentation.details_screen.DetailsViewModel
import com.example.cryptoapp.presentation.main_screen.MainRepository
import com.example.cryptoapp.presentation.main_screen.MainViewModel
import com.example.cryptoapp.presentation.settings_screen.SettingsRepository
import com.example.cryptoapp.presentation.settings_screen.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val appModule = module {

    factory <ApiService> { provideApiService(retrofit = get()) }

    factory  {
        CryptoRemoteDataSource()
    }
    factory<MainRepository> { MainRepository( remoteDataSource =  get(),
        localDataSource = get()
//        db = get(),
        ) }

    factory<CryptoRemotePagingSource> { CryptoRemotePagingSource() }

    factory <CryptoDbDataSource> { CryptoDbDataSource(db = get()) }

    factory <CryptoDbPagingSource> { CryptoDbPagingSource(db = get()) }

    single <CryptoDatabase> { CryptoDatabase.getDatabase(context = androidContext()) }

    factory <DetailsRepository> { DetailsRepository() }

    factory <DetailsViewModel> { DetailsViewModel(repository = get()) }

    factory <SettingsRepository> { SettingsRepository(db = get()) }

    factory <SettingsViewModel> { SettingsViewModel(repository = get()) }
}

fun provideApiService(
    retrofit: Retrofit
): ApiService = retrofit.create(ApiService::class.java)

val viewModelModule = module {
    viewModel { MainViewModel(repository = get()) }
}