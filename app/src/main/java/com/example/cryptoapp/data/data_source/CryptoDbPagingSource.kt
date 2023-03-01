package com.example.cryptoapp.data.data_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptoapp.data.CryptoDatabase
import com.example.cryptoapp.data.entities.CryptoEntity
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.presentation.main_screen.MainRepository
import com.example.cryptoapp.presentation.main_screen.MainViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1

class CryptoDbPagingSource(
    private val db: CryptoDatabase
): PagingSource<Int, CryptoEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoEntity> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val cryptos = mutableListOf<CryptoEntity>()
            coroutineScope {
                runBlocking {
                    cryptos.addAll(db.cryptoDao().getAllCrypto())
                }
            }
            val nextKey =
                if (cryptos!!.isEmpty()) {
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

    override fun getRefreshKey(state: PagingState<Int, CryptoEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}