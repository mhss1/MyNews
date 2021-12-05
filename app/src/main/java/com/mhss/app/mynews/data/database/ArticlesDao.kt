package com.mhss.app.mynews.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mhss.app.mynews.util.Constants

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<DatabaseArticle>)

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY publishedAt DESC")
    fun getGeneralArticles(category: String = Constants.ARTICLE_TYPE_GENERAL): LiveData<List<DatabaseArticle>>

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY publishedAt DESC")
    fun getTechArticles(category: String = Constants.ARTICLE_TYPE_TECH): LiveData<List<DatabaseArticle>>

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY publishedAt DESC")
    fun getEntertainmentArticles(category: String = Constants.ARTICLE_TYPE_ENTERTAINMENT): LiveData<List<DatabaseArticle>>

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY publishedAt DESC")
    fun getSportsArticles(category: String = Constants.ARTICLE_TYPE_SPORTS): LiveData<List<DatabaseArticle>>

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY publishedAt DESC")
    fun getBusinessArticles(category: String = Constants.ARTICLE_TYPE_BUSINESS): LiveData<List<DatabaseArticle>>

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY publishedAt DESC")
    fun getHealthArticles(category: String = Constants.ARTICLE_TYPE_HEALTH): LiveData<List<DatabaseArticle>>

    @Query("SELECT * FROM articles_table WHERE category = :category ORDER BY publishedAt DESC")
    fun getScienceArticles(category: String = Constants.ARTICLE_TYPE_SCIENCE): LiveData<List<DatabaseArticle>>

}