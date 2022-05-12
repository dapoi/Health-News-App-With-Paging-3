package com.dapoi.healthnewsapp.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String = "b5e957cc160649c09671f3dc74c2f3b2"
    ): NewsResponse
}