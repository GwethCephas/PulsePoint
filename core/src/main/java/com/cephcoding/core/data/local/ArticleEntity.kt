package com.cephcoding.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("articleEntities_table")
data class ArticleEntity(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val sourceId: String?,
    val sourceName: String,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)