package com.dapoi.healthnewsapp.source

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dapoi.healthnewsapp.network.ArticlesItem

@Database(entities = [ArticlesItem::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        @JvmStatic
        fun getDatabase(context: android.content.Context): NewsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}