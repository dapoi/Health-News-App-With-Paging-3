package com.dapoi.healthnewsapp.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dapoi.healthnewsapp.data.source.local.NewsDatabase
import com.dapoi.healthnewsapp.data.source.local.RemoteKeys
import com.dapoi.healthnewsapp.data.source.remote.ApiService
import com.dapoi.healthnewsapp.data.source.remote.ArticlesItem


@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsDatabase: NewsDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, ArticlesItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticlesItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPageKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevPageKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextPageKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getNews("Kesehatan", page, state.config.pageSize).articles

            val endOfPaginationReached = responseData.isEmpty()

            newsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDatabase.remoteKeysDao().deleteRemoteKeys()
                    newsDatabase.newsDao().deleteAllNews()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map {
                    RemoteKeys(id = it.title, prevPageKey = prevKey, nextPageKey = nextKey)
                }
                newsDatabase.remoteKeysDao().insert(keys)
                newsDatabase.newsDao().insertNews(responseData)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticlesItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            newsDatabase.remoteKeysDao().getRemoteKeys(data.title)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticlesItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            newsDatabase.remoteKeysDao().getRemoteKeys(data.title)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ArticlesItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.title.let { id ->
                newsDatabase.remoteKeysDao().getRemoteKeys(id.toString())
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}