package com.mhss.app.mynews.ui.veiwmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mhss.app.mynews.data.repository.ArticlesRepository
import com.mhss.app.mynews.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository
    ) : ViewModel(){

    val generalArticles = articlesRepository.generalArticles
    val techArticles = articlesRepository.techArticles
    val sportsArticles = articlesRepository.sportsArticles
    val healthArticles = articlesRepository.healthArticles
    val businessArticles = articlesRepository.businessArticles
    val entertainmentArticles = articlesRepository.entertainmentArticles
    val scienceArticles = articlesRepository.scienceArticles

        fun getArticles(query: String, getList: (List<Article>) -> Unit) = viewModelScope.launch{
            withContext(Dispatchers.IO){
                getList(articlesRepository.getArticles(query))
            }
        }

        fun refreshArticles() = viewModelScope.launch{
                articlesRepository.refreshArticles()
        }
}