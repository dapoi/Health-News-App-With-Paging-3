package com.dapoi.healthnewsapp.data.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dapoi.healthnewsapp.data.source.remote.ArticlesItem

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<ArticlesItem>)

    @Query("SELECT * FROM articles")
    fun getNews(): PagingSource<Int, ArticlesItem>

    @Query("DELETE FROM articles")
    suspend fun deleteAllNews()
}