package com.mhss.app.mynews.ui.veiwmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mhss.app.mynews.data.repository.ArticlesRepository
import com.mhss.app.mynews.domain.Article
import com.mhss.app.mynews.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : ViewModel() {

    private val _refreshState = MutableLiveData<DataState<Nothing?>>()
    val refreshState: LiveData<DataState<Nothing?>>
        get() = _refreshState

    val generalArticles = articlesRepository.generalArticles
    val techArticles = articlesRepository.techArticles
    val sportsArticles = articlesRepository.sportsArticles
    val healthArticles = articlesRepository.healthArticles
    val businessArticles = articlesRepository.businessArticles
    val entertainmentArticles = articlesRepository.entertainmentArticles
    val scienceArticles = articlesRepository.scienceArticles

    fun getArticles(query: String) =
        articlesRepository
            .getArticles(query.filter { !it.isWhitespace() })
            .asLiveData()

    fun refreshArticles() = viewModelScope.launch {
        try {
            _refreshState.value = DataState.Loading
            articlesRepository.refreshArticles()
            _refreshState.value = DataState.Success(null)
        }catch (io: IOException){
            _refreshState.value = DataState.Error("Couldn't refresh. Check your internet connection an try again")
        }catch (e: Exception){
            _refreshState.value = DataState.Error("Couldn't refresh. Unexpected Error has happened.")
        }

    }
}