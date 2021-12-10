package com.mhss.app.mynews.ui.veiwmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mhss.app.mynews.data.repository.ArticlesRepository
import com.mhss.app.mynews.domain.Article
import com.mhss.app.mynews.util.DataState
import com.mhss.app.mynews.util.countryToCode
import com.mhss.app.mynews.util.languageToCode
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : ViewModel() {

    private val _refreshState = MutableLiveData<DataState<Nothing?>>()
    val refreshState: LiveData<DataState<Nothing?>>
        get() = _refreshState

    private val _currentQuery = MutableLiveData("" to "")

    val generalArticles = articlesRepository.generalArticles
    val techArticles = articlesRepository.techArticles
    val sportsArticles = articlesRepository.sportsArticles
    val healthArticles = articlesRepository.healthArticles
    val businessArticles = articlesRepository.businessArticles
    val entertainmentArticles = articlesRepository.entertainmentArticles
    val scienceArticles = articlesRepository.scienceArticles

    fun articlesByQuery() = _currentQuery.switchMap {
        println("lang is : ${it.second}")
        articlesRepository.getArticles(it.first, it.second.languageToCode()).asLiveData()
    }

    private val _lastResult: MutableLiveData<List<Article>?> = MutableLiveData(null)
    val lastResult: LiveData<List<Article>?>
        get() = _lastResult

    fun refreshArticles(country: String) = viewModelScope.launch {
        try {
            _refreshState.value = DataState.Loading
            articlesRepository.refreshArticles(country.countryToCode())
            _refreshState.value = DataState.Success(null)
        }catch (io: IOException){
            _refreshState.value = DataState.Error("Couldn't refresh. Check your internet connection an try again")
        }catch (e: Exception){
            _refreshState.value = DataState.Error("Couldn't refresh. Unexpected Error has happened.")
        }

    }

    fun searchArticles(query: String, language: String) {
        _currentQuery.value = query to language
    }

    fun setLastResult(list: List<Article>?){
        _lastResult.value = list
    }
}