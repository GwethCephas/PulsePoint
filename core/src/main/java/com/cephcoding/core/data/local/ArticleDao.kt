package com.cephcoding.core.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {

    @Upsert
    suspend fun addFavoriteArticle(articleEntity: ArticleEntity)

    @Delete
    suspend fun deleteArticle(articleEntity: ArticleEntity)

    @Query("SELECT * FROM articleEntities_table")
    fun getAllFavoriteArticles(): PagingSource<Int, ArticleEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM articleEntities_table WHERE url = :url)")
    suspend fun isArticleFavorite(url: String): Boolean

    @Query("SELECT url FROM articleEntities_table")
    fun getAllArticlesUrls(): Flow<List<String>>

}