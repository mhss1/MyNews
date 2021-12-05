package com.mhss.app.mynews.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseArticle::class], version = 1)
abstract class ArticlesDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao
}