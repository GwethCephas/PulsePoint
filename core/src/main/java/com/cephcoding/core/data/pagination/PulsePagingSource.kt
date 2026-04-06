package com.cephcoding.core.data.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cephcoding.core.data.remote.ApiService
import com.cephcoding.core.domain.model.Article


class PulsePagingSource(
    private val apiService: ApiService
): PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {

            val currentPage = params.key ?: 1
            val response = apiService.getTopHeadlines(
                page = currentPage,
                pageSize = params.loadSize
            )
            val endOfPaginationReached = response.articles.isEmpty()

            val nextPage = if(endOfPaginationReached) null else currentPage + 1
            val prevPage = if(currentPage == 1) null else currentPage - 1

            LoadResult.Page(
                data = response.articles,
                prevKey = prevPage,
                nextKey = nextPage
            )

        }catch (e:Exception){
            Log.e("PagingSource", " An error occurred fetching data- ${e.message}")
            LoadResult.Error(e)
        }

    }
}