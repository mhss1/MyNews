package com.mhss.app.mynews.data.network

import com.mhss.app.mynews.BuildConfig.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlinesByCategory(
        @Query("category") category: String,
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY
    ): ArticlesDto

    @GET("top-headlines")
    suspend fun getArticles(
        @Query("q") query: String,
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "relevancy",
        @Query("apiKey") apiKey: String = API_KEY
    ): ArticlesDto


}