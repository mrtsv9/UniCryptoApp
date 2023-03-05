package com.example.cryptoapp.data.data_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.data.entities.toCryptoItem
import com.example.cryptoapp.domain.RetrofitInstance
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.domain.dto.toCryptoEntity
import com.example.cryptoapp.domain.dto.toCryptoItem
import com.example.cryptoapp.domain.service.ApiService
import com.example.cryptoapp.presentation.item.CryptoItem
import com.example.cryptoapp.presentation.main_screen.MainRepository
import com.example.cryptoapp.presentation.main_screen.MainViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1
private var networkPageIndex = 2

class CryptoDbPagingSource(
    private val db: CryptoDatabase,
    private var isFromDb: Boolean
) : PagingSource<Int, CryptoItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoItem> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val cryptos = mutableListOf<CryptoItem>()
            if (isFromDb) {
                coroutineScope {
                    cryptos.addAll(db.cryptoDao().getAllCrypto().map { it.toCryptoItem() })
                }
                isFromDb = false
            } else {
                val retrofit = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
                val response = retrofit.getCryptosByPage(
                    page = networkPageIndex.toString()
                )
                response.body()?.map { it.toCryptoItem() }?.let { cryptos.addAll(it) }
                networkPageIndex.inc()
            }
            val nextKey =
                if (cryptos.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / PAGE_SIZE)
                }
            LoadResult.Page(
                data = cryptos,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CryptoItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}