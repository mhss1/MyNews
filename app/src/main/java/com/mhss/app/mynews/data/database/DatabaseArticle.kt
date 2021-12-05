package com.mhss.app.mynews.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mhss.app.mynews.domain.Article
import com.mhss.app.mynews.util.Constants
import java.text.SimpleDateFormat
import java.util.*

fun List<DatabaseArticle>.toArticles() =
    map {
        Article(
            title = it.title,
            author = it.author,
            content = it.content,
            source = it.source,
            publishedAt = it.publishedAtString,
            url = it.url,
            imageUrl = it.imageUrl
        )
    }

@Entity(tableName = "articles_table")
data class DatabaseArticle(
    val title: String,
    val author: String,
    val content: String,
    val publishedAt: Long,
    val source: String,
    val imageUrl: String,
    var category: String = Constants.ARTICLE_TYPE_GENERAL,
    @PrimaryKey(autoGenerate = false)
    val url: String,
) {
    val publishedAtString : String
        get() {
            val outputFormat = SimpleDateFormat("MMM dd,yyyy", Locale.getDefault())
            return outputFormat.format(publishedAt)
        }
}