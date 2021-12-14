package com.mhss.app.mynews.data.repository

import androidx.lifecycle.Transformations
import com.mhss.app.mynews.data.database.ArticlesDao
import com.mhss.app.mynews.data.database.toArticles
import com.mhss.app.mynews.data.network.NewsApi
import com.mhss.app.mynews.util.Constants
import com.mhss.app.mynews.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.flow
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

    fun getArticles(query: String, language: String) = flow {
        try{
            if (query.isBlank())
                emit(DataState.Success(emptyList()))
            else {
                emit(DataState.Loading)
                val articlesResponse = newsApi.getArticles(query, language)
                emit(DataState.Success(articlesResponse.toArticles()))
            }
        }catch (io: IOException){
            emit(DataState.Error("Couldn't get results. Check your internet connection an try again"))
        }catch (e: Exception){
            emit(DataState.Error("Unexpected Error has happened"))
        }
    }

    suspend fun refreshArticles(country: String){
        withContext(Dispatchers.IO) {
            val generalArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_GENERAL, country)
            val sportsArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_SPORTS, country)
            val techArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_TECH, country)
            val healthArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_HEALTH, country)
            val entertainmentArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_ENTERTAINMENT, country)
            val scienceArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_SCIENCE, country)
            val businessArticles = newsApi.getTopHeadlinesByCategory(Constants.ARTICLE_TYPE_BUSINESS, country)

            articlesDao.clearCache()

            articlesDao.insertArticles(generalArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_GENERAL })
            articlesDao.insertArticles(sportsArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_SPORTS })
            articlesDao.insertArticles(techArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_TECH })
            articlesDao.insertArticles(healthArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_HEALTH })
            articlesDao.insertArticles(entertainmentArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_ENTERTAINMENT })
            articlesDao.insertArticles(scienceArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_SCIENCE })
            articlesDao.insertArticles(businessArticles.toDatabaseArticles()
                .onEach { it.category = Constants.ARTICLE_TYPE_BUSINESS })

        }
    }
}