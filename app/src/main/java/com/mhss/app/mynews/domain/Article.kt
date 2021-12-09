package com.mhss.app.mynews.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Article(
    val title: String,
    val author: String,
    val content: String,
    val publishedAt: String,
    val source: String,
    val url: String,
    val imageUrl: String
) : Parcelable