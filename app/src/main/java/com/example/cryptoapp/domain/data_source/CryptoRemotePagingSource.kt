package com.example.cryptoapp.domain.data_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptoapp.domain.RetrofitInstance
import com.example.cryptoapp.domain.dto.CryptoResponse
import com.example.cryptoapp.domain.service.ApiService
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1

class CryptoRemotePagingSource: PagingSource<Int, CryptoResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoResponse> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val retrofit = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
            val response = retrofit.getCryptosByPage(
                page = pageIndex.toString()
            )
            val cryptos = response.body()
            val nextKey =
                if (cryptos!!.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
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

    override fun getRefreshKey(state: PagingState<Int, CryptoResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}