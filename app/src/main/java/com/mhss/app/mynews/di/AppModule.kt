package com.mhss.app.mynews.di

import android.content.Context
import androidx.room.Room
import com.mhss.app.mynews.data.database.ArticlesDatabase
import com.mhss.app.mynews.data.network.NewsApi
import com.mhss.app.mynews.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNotesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ArticlesDatabase::class.java, Constants.DB_NAME)
            .build()

    @Singleton
    @Provides
    fun provideDoa(db: ArticlesDatabase) = db.articlesDao()
}