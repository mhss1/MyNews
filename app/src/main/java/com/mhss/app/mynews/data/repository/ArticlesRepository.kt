package com.mhss.app.mynews.data.repository

import androidx.lifecycle.Transformations
import com.mhss.app.mynews.data.database.ArticlesDao
import com.mhss.app.mynews.data.database.toArticles
import com.mhss.app.mynews.data.network.NewsApi
import com.mhss.app.mynews.domain.Article
import com.mhss.app.mynews.util.Constants
import com.mhss.app.mynews.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

class ArticlesRepository @Inject constructor(
    private val articlesDao: ArticlesDao,
    private val newsApi: NewsApi
    )
{

    val generalArticles = Transformations.map(articlesDao.getGeneralArticles()){
        it.toArticles()
    }
    val techArticles = Transformations.map(articlesDao.getTechArticles()){
        it.toArticles()
    }
    val sportsArticles = Transformations.map(articlesDao.getSportsArticles()){
        it.toArticles()
    }
    val healthArticles = Transformations.map(articlesDao.getHealthArticles()){
        it.toArticles()
    }
    val businessArticles = Transformations.map(articlesDao.getBusinessArticles()){
        it.toArticles()
    }
    val entertainmentArticles = Transformations.map(articlesDao.getEntertainmentArticles()){
        it.toArticles()
    }
    val scienceArticles = Transformations.map(articlesDao.getScienceArticles()){
        it.toArticles()
    }

    fun getArticles(query: String) : Flow<DataState<List<Article>>> = flow {
            emit(DataState.Loading)
        try{
            val articlesResponse = newsApi.getArticles(query)
            emit(DataState.Success(articlesResponse.toArticles()))
        }catch (io: IOException){
            emit(DataState.Error("Couldn't get results. Check your internet connection an try again"))
        }catch (e: Exception){
            emit(DataState.Error("Unknown Error has happened"))
        }
    }

    suspend fun refreshArticles(){
        withContext(Dispatchers.IO) {
            val generalArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_GENERAL)
            articlesDao.insertArticles(generalArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_GENERAL })

            val sportsArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_SPORTS)
            articlesDao.insertArticles(sportsArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_SPORTS })

            val techArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_TECH)
            articlesDao.insertArticles(techArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_TECH })

            val healthArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_HEALTH)
            articlesDao.insertArticles(healthArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_HEALTH })

            val entertainmentArticles =
                newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_ENTERTAINMENT)
            articlesDao.insertArticles(entertainmentArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_ENTERTAINMENT })

            val scienceArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_SCIENCE)
            articlesDao.insertArticles(scienceArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_SCIENCE })

            val businessArticles =
                newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_BUSINESS)
            articlesDao.insertArticles(businessArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_BUSINESS })
        }
    }
}