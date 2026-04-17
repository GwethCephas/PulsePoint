package com.cephcoding.features.feat_favorite

import androidx.paging.PagingData
import app.cash.turbine.test
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
import io.mockk.verify
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
class FavoriteViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var repository: PulseRepository

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
        coEvery { repository.getFavoriteNews() } returns flowOf(PagingData.from(mockArticles))
        every { repository.getFavoriteArticlesUrls() } returns flowOf(emptyList())

        favoriteViewModel = FavoriteViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun `favorite articles are loaded correctly`() = runTest {

        advanceUntilIdle()

        favoriteViewModel.favoriteArticles.test {
            val articles = awaitItem()
            assertThat(articles).isNotNull()

        }

        coVerify(atLeast = 1) { repository.getFavoriteNews() }

    }

    @Test
    fun `favorite articles urls are loaded correctly`() = runTest {
        val urls = listOf("url1", "url2")

        every { repository.getFavoriteArticlesUrls() } returns flowOf(urls)

        favoriteViewModel = FavoriteViewModel(repository)


        val result = favoriteViewModel.favoriteArticlesUrls.first { it.isNotEmpty() }

        assertThat(result).isEqualTo(urls)
        coVerify { repository.getFavoriteArticlesUrls() }
    }

    @Test
    fun `toggleFavoriteArticle updates favoriteArticlesUrls correctly`() = runTest {
        val article = mockArticles[0]

        coEvery { repository.toggleFavoriteStatus(article) } just runs

        favoriteViewModel.toggleFavoriteStatus(article)

        advanceUntilIdle()

        coVerify(atLeast = 1) { repository.toggleFavoriteStatus(article) }
    }
}




