package com.cephcoding.core.data.mappers

import com.cephcoding.core.data.local.ArticleEntity
import com.cephcoding.core.domain.model.Article
import com.cephcoding.core.domain.model.Source


fun Article.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        url = this.url,
        sourceId = this.source.id,
        sourceName = this.source.name,
        title = this.title,
        author = this.author,
        content = this.content,
        description = this.description,
        publishedAt = this.publishedAt,
        urlToImage = this.urlToImage
    )
}


fun ArticleEntity.toArticle(): Article {
    return Article(
        url = this.url,
        source = Source(
            this.sourceId,
            this.sourceName
        ),
        title = this.title,
        author = this.author,
        content = this.content,
        description = this.description,
        publishedAt = this.publishedAt,
        urlToImage = this.urlToImage
    )
}