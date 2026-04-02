package com.ceph.pulsepoint.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ceph.pulsepoint.data.local.ArticleDatabase
import com.ceph.pulsepoint.data.mappers.toArticle
import com.ceph.pulsepoint.data.mappers.toArticleEntity
import com.ceph.pulsepoint.data.pagination.CategoriesPagingSource
import com.ceph.pulsepoint.data.pagination.PulsePagingSource
import com.ceph.pulsepoint.data.pagination.SearchPagingSource
import com.ceph.pulsepoint.data.remote.ApiService
import com.ceph.pulsepoint.domain.model.Article
import com.ceph.pulsepoint.domain.repository.PulseRepository
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