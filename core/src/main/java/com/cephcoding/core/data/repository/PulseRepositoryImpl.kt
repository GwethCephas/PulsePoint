package com.cephcoding.core.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.cephcoding.core.data.local.ArticleDatabase
import com.cephcoding.core.data.mappers.toArticle
import com.cephcoding.core.data.mappers.toArticleEntity
import com.cephcoding.core.data.pagination.CategoriesPagingSource
import com.cephcoding.core.data.pagination.PulsePagingSource
import com.cephcoding.core.data.pagination.SearchPagingSource
import com.cephcoding.core.data.remote.ApiService
import com.cephcoding.core.domain.model.Article
import com.cephcoding.core.domain.repository.PulseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PulseRepositoryImpl(
    private val apiService: ApiService,
    private val articleDatabase: ArticleDatabase
) : PulseRepository {

    private val favoritesDao = articleDatabase.dao

    override fun getNewsHeadlines(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {
                PulsePagingSource(apiService)
            }
        ).flow
    }


    override fun getSearchedNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = {
                SearchPagingSource(
                    apiService = apiService,
                    query = query
                )
            }

        ).flow
    }

    override fun getFavoriteNews(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {
                favoritesDao.getAllFavoriteArticles()
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toArticle() }
            }
    }

    override fun getNewsByCategory(category: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {
                CategoriesPagingSource(apiService = apiService, category = category)
            }
        ).flow
    }

    override fun getFavoriteArticlesUrls(): Flow<List<String>> {
        return favoritesDao.getAllArticlesUrls()
    }

    override suspend fun toggleFavoriteStatus(article: Article) {
        val isFavorite = favoritesDao.isArticleFavorite(article.url)
        val favoriteEntity = article.toArticleEntity()


        if (isFavorite) {
            favoritesDao.deleteArticle(favoriteEntity)
        } else {
            favoritesDao.addFavoriteArticle(favoriteEntity)
        }
    }

    override fun getTodayNews(from: String, to:String): Flow<List<Article>> {
        return flow {
            try {
                val todayNews = apiService.getTodayNews(from, to)
                emit(todayNews.articles)
                Log.d("PulseRepositoryImpl", "To day's news fetched successfully : ${todayNews.articles}")
            } catch (e: Exception) {
                Log.e("PulseRepositoryImpl", "Error fetching today's news: ${e.message}")
            }
        }
    }
}