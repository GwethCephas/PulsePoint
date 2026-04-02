package com.ceph.pulsepoint.data.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ceph.pulsepoint.data.remote.ApiService
import com.ceph.pulsepoint.domain.model.Article

class CategoriesPagingSource(
    private val apiService: ApiService,
    private val category: String
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val currentPage = params.key ?: 1

            val response = apiService.getNewsByCategory(
                category = category,
                page = currentPage,
                pageSize = params.loadSize
            )
            Log.d("CategoriesPagingSource", "Response: $response")

            val articles = response.articles

            val endOfPaginationReached = articles.isEmpty()
            val nextPage = if (endOfPaginationReached) null else currentPage + 1
            val prevPage = if (currentPage == 1) null else currentPage - 1

            LoadResult.Page(
                data = articles,
                prevKey = prevPage,
                nextKey = nextPage
            )

        } catch (e: Exception) {
            Log.e("CategoriesPagingSource", "Error: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}