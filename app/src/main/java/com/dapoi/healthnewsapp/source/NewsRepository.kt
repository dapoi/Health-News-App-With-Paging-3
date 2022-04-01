package com.dapoi.healthnewsapp.source

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dapoi.healthnewsapp.network.ApiService
import com.dapoi.healthnewsapp.network.ArticlesItem

class NewsRepository(private val newsDB: NewsDatabase, private val apiService: ApiService) {

    fun getNews(): LiveData<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = { NewsPagingSource(apiService) }
        ).liveData
    }
}