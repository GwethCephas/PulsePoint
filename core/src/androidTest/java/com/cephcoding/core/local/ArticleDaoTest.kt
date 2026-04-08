package com.cephcoding.core.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cephcoding.core.data.local.ArticleDao
import com.cephcoding.core.data.local.ArticleDatabase
import com.cephcoding.core.data.local.ArticleEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ArticleDaoTest {
    private lateinit var articleDao: ArticleDao
    private lateinit var database: ArticleDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            ArticleDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        articleDao = database.dao

    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addFavoriteArticle() = runTest {
        val article = ArticleEntity(
            url = "url1",
            sourceId = "id",
            sourceName = "name",
            author = "author",
            title = "title",
            description = "description",
            urlToImage = "image",
            publishedAt = "2023-01-01",
            content = "content"
        )
        articleDao.addFavoriteArticle(article)

        val isFavorite = articleDao.isArticleFavorite("url1")
        assertThat(isFavorite).isTrue()

    }

    @Test
    fun deleteArticle() = runTest {
        val article = ArticleEntity(
            url = "url1",
            sourceId = "id",
            sourceName = "name",
            author = "author",
            title = "title",
            description = "description",
            urlToImage = "image",
            publishedAt = "2023-01-01",
            content = "content"
        )
        articleDao.addFavoriteArticle(article)
        articleDao.deleteArticle(article)

        val isFavorite = articleDao.isArticleFavorite("url1")
        assertThat(isFavorite).isFalse()

    }

    @Test
    fun getAllArticlesUrls() = runTest {
        val articles = listOf(
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

        articles.forEach { articleDao.addFavoriteArticle(it) }

        val result = articleDao.getAllArticlesUrls().first()

        assertThat(result).containsExactly("url1", "url2", "url3")
    }


    @Test
    fun isArticleFavorite_returnsTrueWhenFavorite() = runTest {
        val article = ArticleEntity(
            url = "url1",
            sourceId = "id",
            sourceName = "name",
            author = "author",
            title = "title",
            description = "description",
            urlToImage = "image",
            publishedAt = "2023-01-01",
            content = "content"
        )
        articleDao.addFavoriteArticle(article)
        val result = articleDao.isArticleFavorite("url1")
        assertThat(result).isTrue()
    }

    @Test
    fun isArticleFavorite_returnsFalseWhenNotFavorite() = runTest {
        val result = articleDao.isArticleFavorite("non_existent_url")
        assertThat(result).isFalse()
    }


}