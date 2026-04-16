package com.cephcoding.features.feat_home

import androidx.paging.PagingData
import com.cephcoding.core.data.local.ArticleEntity
import com.cephcoding.core.data.mappers.toArticle
import com.cephcoding.core.domain.repository.PulseRepository
import com.cephcoding.features.common.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PulseRepository
    private lateinit var viewModel: HomeViewModel

    private val mockArticles = listOf(
        ArticleEntity(
            "url1", "id1", "name1", "author1", "title1", "desc1", "img1", "2023-01-01", "content1"
        ),
        ArticleEntity(
            "url2", "id2", "name2", "author2", "title2", "desc2", "img2", "2023-01-02", "content2"
        )
    ).map { it.toArticle() }

    @Before
    fun setUp() {
        repository = mockk()
        
        coEvery { repository.getNewsHeadlines() } returns flowOf(PagingData.from(mockArticles))
        every { repository.getFavoriteArticlesUrls() } returns flowOf(emptyList())
        
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getNewsHeadlines updates the articles state correctly`() = runTest {
        advanceUntilIdle()

        val articlesState = viewModel.articles.value

        assertThat(articlesState).isNotNull()
        coVerify(atLeast = 1) { repository.getNewsHeadlines() }
    }

    @Test
    fun `getNewsByCategory updates the categoryNews state correctly`() = runTest {
        val category = "technology"
        val pagingData = PagingData.from(mockArticles)
        coEvery { repository.getNewsByCategory(category) } returns flowOf(pagingData)

        viewModel.getNewsByCategory(category)
        advanceUntilIdle()

        val categoryNewsState = viewModel.categoryNews.value

        assertThat(categoryNewsState).isNotNull()
        coVerify(exactly = 1) { repository.getNewsByCategory(category) }
    }

    @Test
    fun `favoriteArticlesUrls should reflect the list of URLs from repository`() = runTest {
        val urls = listOf("url1", "url2")
        every { repository.getFavoriteArticlesUrls() } returns flowOf(urls)
        
        viewModel = HomeViewModel(repository)

        val result = viewModel.favoriteArticlesUrls.first { it.isNotEmpty() }

        assertThat(result).isEqualTo(urls)
        coVerify { repository.getFavoriteArticlesUrls() }
    }

    @Test
    fun `toggleFavoriteStatus calls repository toggleFavoriteStatus`() = runTest {
        val article = mockArticles[0]
        coEvery { repository.toggleFavoriteStatus(article) } just runs

        viewModel.toggleFavoriteStatus(article)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.toggleFavoriteStatus(article) }
    }
}
