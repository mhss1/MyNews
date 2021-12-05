package com.mhss.app.mynews.data.network

import com.mhss.app.mynews.data.database.DatabaseArticle
import com.mhss.app.mynews.domain.Article

data class ArticlesDto(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
){
    fun toDatabaseArticles() =
        articles.map {
            DatabaseArticle(
                title = it.title,
                author = it.author ?: "",
                content = it.content ?: "",
                source = it.source.name ?: "",
                publishedAt = it.publishedAtLong,
                url = it.url,
                imageUrl = it.urlToImage ?: ""
            )
        }

    fun toArticles() =
        articles.map {
             Article(
                 title = it.title,
                author = it.author ?: "",
                 content = it.content ?: "",
                 source = it.source.name ?: "",
                 publishedAt = it.publishedAtFormatted,
                 url = it.url,
                 imageUrl = it.urlToImage ?: ""
             )
        }
}