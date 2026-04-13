package com.cephcoding.core.data.pagination

import android.util.Log
import androidx.paging.PagingSource
import com.cephcoding.core.data.local.ArticleEntity
import com.cephcoding.core.data.mappers.toArticle
import com.cephcoding.core.data.remote.ApiService
import com.cephcoding.core.domain.model.Article
import com.cephcoding.core.domain.model.NewsResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CategoriesPagingSourceTest {

    private val apiService: ApiService = mockk()

    private val articles = listOf(
        ArticleEntity(
            "url1",
            "id1",
            "name1",
            "author1",
            "title1",
            "desc1",
            "img1",
            "2023-01-01",
            "content1"
        ),
        ArticleEntity(
            "url2",
            "id2",
            "name2",
            "author2",
            "title2",
            "desc2",
            "img2",
            "2023-01-02",
            "content2"
        ),
        ArticleEntity(
            "url3",
            "id3",
            "name3",
            "author3",
            "title3",
            "desc3",
            "img3",
            "2023-01-03",
            "content3"
        )
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `load method returns categories Page with data when successful`() = runTest {
        val mockArticles = articles.map { it.toArticle() }
        val newsResponse = NewsResponse("ok", 3, mockArticles)

        coEvery {
            apiService.getNewsByCategory(any(), any(), any(), any())
        } returns newsResponse

        val pagingSource = CategoriesPagingSource(apiService, "category")
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = mockArticles,
            prevKey = null,
            nextKey = 2
        )

        assertEquals(result, expected)

    }

    @Test
    fun `load method returns an error when API call fails`() = runTest {
        val exception = Exception("API call failed")

        coEvery {
            apiService.getNewsByCategory(any(), any(), any(), any())
        } throws exception

        val pagingSource = CategoriesPagingSource(apiService, "category")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Error<Int, Article>(exception)

        assertEquals(result, expected)

    }

}





