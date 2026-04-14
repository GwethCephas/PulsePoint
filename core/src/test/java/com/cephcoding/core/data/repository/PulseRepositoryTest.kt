package com.cephcoding.core.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.testing.asSnapshot
import com.cephcoding.core.data.local.ArticleDao
import com.cephcoding.core.data.local.ArticleDatabase
import com.cephcoding.core.data.local.ArticleEntity
import com.cephcoding.core.data.mappers.toArticle
import com.cephcoding.core.data.remote.ApiService
import com.cephcoding.core.domain.model.NewsResponse
import com.cephcoding.core.domain.repository.PulseRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class PulseRepositoryTest {
    private lateinit var repository: PulseRepository
    private val apiService: ApiService = mockk()
    private val articleDatabase: ArticleDatabase = mockk()
    private val dao: ArticleDao = mockk()

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

        every { articleDatabase.dao } returns dao
        repository = PulseRepositoryImpl(apiService, articleDatabase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getNewsHeadlines gets news headlines successfully`() = runTest {
        val newsResponse = NewsResponse("ok", 3, articles.map { it.toArticle() })

        coEvery {
            apiService.getTopHeadlines(any(), any(), any(), any())
        } returns newsResponse

        val result = repository.getNewsHeadlines().asSnapshot()

        assertThat(result).isNotNull()

        coVerify(atLeast = 1) { apiService.getTopHeadlines(any(), any(), any(), any()) }

    }

    @Test
    fun `getSearchedNews gets searched news successfully`() = runTest {
        val newsResponse = NewsResponse("ok", 3, articles.map { it.toArticle() })

        coEvery {
            apiService.getSearchedNews(any(), any(), any(), any())
        } returns newsResponse

        val result = repository.getSearchedNews("query").asSnapshot()

        assertThat(result).isNotNull()
        coVerify(atLeast = 1) { apiService.getSearchedNews(any(), any(), any(), any()) }


    }

    @Test
    fun `getFavoriteNews gets favorite news successfully`() = runTest {

        val mockPagingSource = mockk<PagingSource<Int, ArticleEntity>>()

        every { mockPagingSource.registerInvalidatedCallback(any()) } just runs
        every { mockPagingSource.unregisterInvalidatedCallback(any()) } just runs

        coEvery { mockPagingSource.load(any()) } returns PagingSource.LoadResult.Page(
            data = articles,
            prevKey = null,
            nextKey = null
        )

        every { mockPagingSource.getRefreshKey(any()) } returns null
        every { mockPagingSource.invalid } returns false

        every { dao.getAllFavoriteArticles() } returns mockPagingSource
        val result = repository.getFavoriteNews().asSnapshot()

        assertThat(result).isNotEmpty()
        verify(atLeast = 1) { dao.getAllFavoriteArticles() }

    }

    @Test
    fun `getNewsByCategory gets news by category successfully`() = runTest {
        val newsResponse = NewsResponse("ok", 3, articles.map { it.toArticle() })

        coEvery {
            apiService.getNewsByCategory(any(), any(), any(), any())
        } returns newsResponse

        val result = repository.getNewsByCategory("category").asSnapshot()

        assertThat(result).isNotNull()
        coVerify(atLeast = 1) { apiService.getNewsByCategory(any(), any(), any(), any()) }

    }

    @Test
    fun `getFavoriteArticlesUrls should emit a list of favorite article URLs`() = runTest {

        val urls = articles.map { it.url }

        every { dao.getAllArticlesUrls() } returns flowOf(urls)

        val result = repository.getFavoriteArticlesUrls().first()
        assertThat(result).containsExactly("url1", "url2", "url3")
    }

    @Test
    fun `toggleFavoriteStatus adds and removes favorite article based on isFavorite status`() =
        runTest {
            val article = ArticleEntity(
                "url1",
                "id1",
                "name1",
                "author1",
                "title1",
                "desc1",
                "img1",
                "2023-01-01",
                "content1"
            )

            coEvery { dao.isArticleFavorite("url1") } returns false
            coEvery { dao.addFavoriteArticle(any()) } just runs

            repository.toggleFavoriteStatus(article.toArticle())

            coVerify(exactly = 1) { dao.addFavoriteArticle(any()) }
            coVerify(exactly = 0) { dao.deleteArticle(any()) }
        }

    @Test
    fun `getTodayNews according to the date`() = runTest {
        val from = "2023-01-01"
        val to = "2023-01-02"

        val mockArticles = articles.map { it.toArticle() }
        val newsResponse = NewsResponse("ok", 3, mockArticles)

        coEvery { apiService.getTodayNews(from, to) } returns newsResponse

        val flow = repository.getTodayNews(from, to)

        val result = flow.first()

        assertEquals(mockArticles, result)

        coVerify(exactly = 1) { apiService.getTodayNews(from, to) }
    }

}
