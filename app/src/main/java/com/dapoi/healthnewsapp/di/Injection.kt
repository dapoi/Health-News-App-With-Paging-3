package com.dapoi.healthnewsapp.di

import android.content.Context
import com.dapoi.healthnewsapp.network.ApiConfig
import com.dapoi.healthnewsapp.source.NewsDatabase
import com.dapoi.healthnewsapp.source.NewsRepository

object Injection {
    fun provideNewsRepository(context: Context): NewsRepository {
        val db = NewsDatabase.getDatabase(context)
        val apiService = ApiConfig.provideApiService()
        return NewsRepository(db, apiService)
    }
}