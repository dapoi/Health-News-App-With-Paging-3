package com.dapoi.healthnewsapp.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dapoi.healthnewsapp.data.source.remote.ApiService
import com.dapoi.healthnewsapp.data.source.remote.ArticlesItem
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(private val service: ApiService) : PagingSource<Int, ArticlesItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesItem> {
        val page = params.key ?: 1
        return try {
            val response = service.getNews("Health", page, params.loadSize)
            val news = response.articles

            LoadResult.Page(
                data = news,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (news.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticlesItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}