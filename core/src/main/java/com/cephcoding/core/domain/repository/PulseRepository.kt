package com.cephcoding.core.domain.repository

import androidx.paging.PagingData
import com.cephcoding.core.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface PulseRepository {

    fun getNewsHeadlines(): Flow<PagingData<Article>>

    fun getSearchedNews(query: String): Flow<PagingData<Article>>

    fun getFavoriteNews(): Flow<PagingData<Article>>

    fun getNewsByCategory(category: String): Flow<PagingData<Article>>

    fun getFavoriteArticlesUrls(): Flow<List<String>>

    fun getTodayNews(from: String, to: String): Flow<List<Article>>

    suspend fun toggleFavoriteStatus(article: Article)
}