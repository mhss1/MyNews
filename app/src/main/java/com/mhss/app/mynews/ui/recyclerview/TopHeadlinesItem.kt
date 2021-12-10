package com.mhss.app.mynews.ui.recyclerview

import androidx.lifecycle.LiveData
import com.mhss.app.mynews.domain.Article

data class TopHeadlinesItem(
    val category: String,
    val list: LiveData<List<Article>>,
    val icon: Int)