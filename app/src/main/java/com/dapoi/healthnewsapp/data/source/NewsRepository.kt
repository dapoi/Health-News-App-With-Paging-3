package com.dapoi.healthnewsapp.data.source

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dapoi.healthnewsapp.data.source.local.NewsDatabase
import com.dapoi.healthnewsapp.data.source.remote.ApiService
import com.dapoi.healthnewsapp.data.source.remote.ArticlesItem

class NewsRepository(private val newsDB: NewsDatabase, private val apiService: ApiService) {

    @OptIn(ExperimentalPagingApi::class)
    fun getNews(): LiveData<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = NewsRemoteMediator(newsDB, apiService),
            pagingSourceFactory = {
//                NewsPagingSource(apiService)
                newsDB.newsDao().getNews()
            }
        ).liveData
    }
}