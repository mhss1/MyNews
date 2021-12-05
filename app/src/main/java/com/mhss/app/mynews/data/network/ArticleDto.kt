package com.mhss.app.mynews.data.network

import java.text.SimpleDateFormat
import java.util.*

data class ArticleDto(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: SourceDto,
    val title: String,
    val url: String,
    val urlToImage: String?
){
    val publishedAtLong : Long
        get() {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val inputDate = inputFormat.parse(publishedAt)!!
            return inputDate.time
        }

    val publishedAtFormatted: String
        get() {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val inputDate = inputFormat.parse(publishedAt)!!
            val outputFormat = SimpleDateFormat("MMM dd,yyyy", Locale.getDefault())
            return outputFormat.format(inputDate)
        }

}